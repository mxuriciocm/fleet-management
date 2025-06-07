package com.example.fleetmanagement.reports.interfaces.rest;

import com.example.fleetmanagement.iam.interfaces.acl.IamContextFacade;

import com.example.fleetmanagement.reports.domain.model.queries.GetReportByIdQuery;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportsByCarrierIdQuery;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportsByTypeQuery;
import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;
import com.example.fleetmanagement.reports.domain.services.ReportCommandService;
import com.example.fleetmanagement.reports.domain.services.ReportQueryService;
import com.example.fleetmanagement.reports.interfaces.rest.resources.CreateReportResource;
import com.example.fleetmanagement.reports.interfaces.rest.resources.ReportResource;
import com.example.fleetmanagement.reports.interfaces.rest.transform.CreateReportCommandFromResourceAssembler;
import com.example.fleetmanagement.reports.interfaces.rest.transform.ReportResourceFromEntityAssembler;
import com.example.fleetmanagement.vehicles.interfaces.acl.VehicleContextFacade;
import com.example.fleetmanagement.vehicles.interfaces.acl.dto.VehicleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Reports Management Endpoints")
public class ReportsController {
    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;
    private final IamContextFacade iamContextFacade;
    private final VehicleContextFacade vehicleContextFacade;

    public ReportsController(ReportCommandService reportCommandService, ReportQueryService reportQueryService, IamContextFacade iamContextFacade, VehicleContextFacade vehicleContextFacade) {
        this.reportCommandService = reportCommandService;
        this.reportQueryService = reportQueryService;
        this.iamContextFacade = iamContextFacade;
        this.vehicleContextFacade = vehicleContextFacade;
    }

    /**
     * Create a new report.
     * @param resource the resource containing the report details
     * @return ResponseEntity with the created report resource
     */
    @PostMapping
    @Operation(summary = "Create a new report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "User not authenticated or not a carrier")
    })
    public ResponseEntity<ReportResource> createReport(@Valid @RequestBody CreateReportResource resource){
        Long carrierId = getCurrentUserId();
        if (carrierId == 0) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        var user = iamContextFacade.fetchUserById(carrierId);
        if (user.isEmpty() || !user.get().roles().contains("ROLE_CARRIER")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        Long managerId = getManagerIdForCarrier(carrierId);
        var command = CreateReportCommandFromResourceAssembler.toCommandFromResource(resource, carrierId, managerId);
        var report = reportCommandService.handle(command);
        var reportResource = ReportResourceFromEntityAssembler.toResourceFromEntity(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportResource);
    }

    /**
     * Get a report by its ID.
     * @param reportId the ID of the report to retrieve
     * @return ResponseEntity with the report resource if found, or appropriate error status
     */
    @GetMapping("/{reportId}")
    @Operation(summary = "Get a report by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access this report")
    })
    public ResponseEntity<ReportResource> getReportById(@PathVariable Long reportId) {
        var reportOptional = reportQueryService.handle(new GetReportByIdQuery(reportId));
        if (reportOptional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        var report = reportOptional.get();
        if (!canAccessReport(report.getCarrierId(), report.getManagerId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(ReportResourceFromEntityAssembler.toResourceFromEntity(report));
    }

    /**
     * Get all reports created by the authenticated carrier.
     * @return ResponseEntity with a list of report resources if found, or no content status
     */
    @GetMapping("/carrier/reports")
    @Operation(summary = "Get all reports created by the authenticated carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access these reports")
    })
    public ResponseEntity<List<ReportResource>> getAllReports() {
        Long carrierId = getCurrentUserId();
        if (carrierId == 0) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        var user = iamContextFacade.fetchUserById(carrierId);
        if (user.isEmpty() || !user.get().roles().contains("ROLE_CARRIER")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var reports = reportQueryService.handle(new GetReportsByCarrierIdQuery(carrierId));
        if (reports.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = ReportResourceFromEntityAssembler.toResourceFromEntities(reports);
        return ResponseEntity.ok(resources);
    }

    /**
     * Get all reports created by a specific carrier.
     * @param carrierId the ID of the carrier whose reports to retrieve
     * @return ResponseEntity with a list of report resources if found, or appropriate error status
     */
    @GetMapping("/carrier/{carrierId}")
    @Operation(summary = "Get all reports created by a specific carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports found successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access these reports"),
            @ApiResponse(responseCode = "404", description = "No reports found for the specified carrier")
    })
    public ResponseEntity<List<ReportResource>> getReportsByCarrierId(@PathVariable Long carrierId) {
        if (!canAccessReportsByCarrier(carrierId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var reports = reportQueryService.handle(new GetReportsByCarrierIdQuery(carrierId));
        if (reports.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = ReportResourceFromEntityAssembler.toResourceFromEntities(reports);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/manager/{managerId}")
    @Operation(summary = "Get all reports created by a specific manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports found successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access these reports"),
            @ApiResponse(responseCode = "404", description = "No reports found for the specified manager")
    })
    public ResponseEntity<List<ReportResource>> getReportsByManagerId(@PathVariable Long managerId) {
        if (!canAccessReportsByManager(managerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var reports = reportQueryService.handle(new GetReportsByCarrierIdQuery(managerId));
        if (reports.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = ReportResourceFromEntityAssembler.toResourceFromEntities(reports);
        return ResponseEntity.ok(resources);
    }

    /**
     * Get all reports by type.
     * @param type the type of reports to retrieve
     * @return ResponseEntity with a list of report resources if found, or appropriate error status
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get all reports by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports found successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access these reports"),
            @ApiResponse(responseCode = "404", description = "No reports found for the specified type")
    })
    public ResponseEntity<List<ReportResource>> getReportsByType(@PathVariable ReportType type) {
        Long userId = getCurrentUserId();
        if (userId == 0) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        var reports = reportQueryService.handle(new GetReportsByTypeQuery(type));
        var filteredReports = reports.stream()
                .filter(report -> canAccessReport(report.getCarrierId(), report.getManagerId()))
                .toList();
        if (filteredReports.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = ReportResourceFromEntityAssembler.toResourceFromEntities(filteredReports);
        return ResponseEntity.ok(resources);
    }

    /**
     * Get the ID of the current authenticated user.
     * @return the user ID or 0 if isn't authenticated
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { return 0L; }
        String email = authentication.getName();
        return iamContextFacade.fetchUserIdByEmail(email);
    }

    /**
     * Check if a user has a specific role.
     * @param userId the ID of the user to check
     * @param role the role to check for
     * @return true if the user has the role, false otherwise
     */
    private boolean hasRole(Long userId, String role) {
        var user = iamContextFacade.fetchUserById(userId);
        return user.isPresent() && user.get().roles().contains(role);
    }

    /**
     * Check if the current user can access a report.
     * @param reportCarrierId the ID of the carrier who created the report
     * @param reportManagerId the ID of the manager associated with the report
     * @return true if the current user can access the report, false otherwise
     */
    private boolean canAccessReport(Long reportCarrierId, Long reportManagerId) {
        Long userId = getCurrentUserId();
        if (userId == 0) { return false; }
        if (hasRole(userId, "ROLE_ADMIN")) { return true; }
        if (hasRole(userId, "ROLE_MANAGER") && reportManagerId != null && reportManagerId.equals(userId)) { return true; }
        return hasRole(userId, "ROLE_CARRIER") && reportCarrierId.equals(userId);
    }

    private boolean canAccessReportsByCarrier(Long carrierId){
        Long userId = getCurrentUserId();
        if (userId == 0) { return false; }
        if (hasRole(userId, "ROLE_ADMIN")) { return true; }
        if (hasRole(userId, "ROLE_MANAGER")) {
            Long managerIdOfCarrier = getManagerIdForCarrier(carrierId);
            return managerIdOfCarrier != 0L && managerIdOfCarrier.equals(userId);
        }
        return hasRole(userId, "ROLE_CARRIER") && carrierId.equals(userId);
    }

    private boolean canAccessReportsByManager(Long managerId) {
        Long userId = getCurrentUserId();
        if (userId == 0) { return false; }
        if (hasRole(userId, "ROLE_ADMIN")) { return true; }
        return hasRole(userId, "ROLE_MANAGER") && managerId.equals(userId);
    }

    private Long getManagerIdForCarrier(Long carrierId) {
        Optional<VehicleDto> vehicle = vehicleContextFacade.fetchVehicleByCarrierId(carrierId);
        if (vehicle.isPresent()) {
            return vehicle.get().managerId();
        }
        throw new IllegalArgumentException("No manager found for carrier with ID: " + carrierId);
    }
}
