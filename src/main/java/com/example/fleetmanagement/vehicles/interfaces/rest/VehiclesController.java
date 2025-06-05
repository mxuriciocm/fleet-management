package com.example.fleetmanagement.vehicles.interfaces.rest;

import com.example.fleetmanagement.iam.interfaces.acl.IamContextFacade;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByCarrierIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByLicensePlateQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehiclesByManagerIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;
import com.example.fleetmanagement.vehicles.domain.services.VehicleCommandService;
import com.example.fleetmanagement.vehicles.domain.services.VehicleQueryService;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.CreateVehicleResource;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.UpdateVehicleResource;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.VehicleResource;
import com.example.fleetmanagement.vehicles.interfaces.rest.transform.CreateVehicleCommandFromResourceAssembler;
import com.example.fleetmanagement.vehicles.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import com.example.fleetmanagement.vehicles.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
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

@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicles Management Endpoints")
public class VehiclesController {
    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;
    private final IamContextFacade iamContextFacade;

    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService, IamContextFacade iamContextFacade) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Create a new vehicle.
     * Only managers can create vehicles, and they must not have reached their vehicle limit.
     * @param resource the CreateVehicleResource containing the vehicle data.
     * @return ResponseEntity containing the created VehicleResource if successful, or an error response.
     *
     */
    @PostMapping
    @Operation(summary = "Create a new vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle data provided"),
            @ApiResponse(responseCode = "401", description = "User not authenticated or not a manager")
    })
    public ResponseEntity<VehicleResource> createVehicle(@Valid @RequestBody CreateVehicleResource resource) {
        Long userId = getCurrentUserId();
        if (userId == null) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        if (!hasRole(userId, "ROLE_MANAGER")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        boolean isPro = false;
        if (vehicleCommandService.hasReachedVehicleLimit(userId, isPro)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var command = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var vehicle = vehicleCommandService.handle(command);
        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleResource);
    }


    /**
     * Get a vehicle by its ID.
     * @param vehicleId the ID of the vehicle to retrieve
     * @return ResponseEntity containing the VehicleResource if found, or 404 Not Found if not found.
     */
    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get vehicle by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
    })
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable Long vehicleId){
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), vehicle.getCarrierId())){ return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }


    /**
     * Get a vehicle by its license plate.
     * @param licensePlate the license plate of the vehicle to retrieve
     * @return ResponseEntity containing the VehicleResource if found, or 404 Not Found if not found.
     */
    @GetMapping("/license-plate/{licensePlate}")
    @Operation(summary = "Get vehicle by license plate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
    })
    public ResponseEntity<VehicleResource> getVehicleByLicensePlate(@PathVariable String licensePlate){
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByLicensePlateQuery(licensePlate));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), vehicle.getCarrierId())){ return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }


    /**
     * Get all vehicles for the authenticated manager.
     * @return ResponseEntity containing a list of VehicleResource if found, or 204 No Content if no vehicles found.
     */
    @GetMapping("/manager/vehicles")
    @Operation(summary = "Get all vehicles for the authenticated manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles found and returned successfully"),
            @ApiResponse(responseCode = "204", description = "Not vehicles found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated or not a manager")
    })
    public ResponseEntity<List<VehicleResource>> getAllVehiclesForManager() {
        Long userId = getCurrentUserId();
        if (userId == 0) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        if (!hasRole(userId, "ROLE_MANAGER")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var vehicles = vehicleQueryService.handle(new GetVehiclesByManagerIdQuery(userId));
        if (vehicles.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = VehicleResourceFromEntityAssembler.toResourceFromEntities(vehicles);
        return ResponseEntity.ok(resources);
    }


    /**
     * Get the assigned vehicle for the authenticated carrier.
     * @return ResponseEntity containing the VehicleResource if found, or 404 Not Found if not found.
     */
    @GetMapping("/carrier/vehicle")
    @Operation(summary = "Get the assigned vehicle for the authenticated carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not assigned to the carrier"),
            @ApiResponse(responseCode = "401", description = "User not authenticated or not a carrier")
    })
    public ResponseEntity<VehicleResource> getAssignedVehicleForCarrier() {
        Long userId = getCurrentUserId();
        if (userId == 0) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        if (!hasRole(userId, "ROLE_CARRIER")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        return vehicleQueryService.handle(new GetVehicleByCarrierIdQuery(userId))
                .map(vehicle -> ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle)))
                .orElse(ResponseEntity.noContent().build());
    }


    /**
     * Get a vehicle assigned to a specific carrier.
     * @param carrierId the ID of the carrier to retrieve the vehicle for
     * @return ResponseEntity containing the VehicleResource if found, or 404 Not Found if not found.
     */
    @GetMapping("/carrier/{carrierId}/vehicle")
    @Operation(summary = "Get vehicle assigned to a specific carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not assigned to the carrier"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access this vehicle")
    })
    public ResponseEntity<VehicleResource> getVehicleByCarrierId(@PathVariable Long carrierId) {
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByCarrierIdQuery(carrierId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), null)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build();}
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }


    /**
     * Get all vehicles for a specific manager.
     * Only accessible by Admins.
     * @param managerId the ID of the manager to retrieve vehicles for
     * @return ResponseEntity containing a list of VehicleResource if found, or 404 Not Found if not found.
     */
    @GetMapping("/manager/{managerId}/vehicles")
    @Operation(summary = "Get all vehicles for a specific manager (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found or no vehicles assigned"),
            @ApiResponse(responseCode = "403", description = "User not authorized to access this manager's vehicles")
    })
    public ResponseEntity<List<VehicleResource>> getVehiclesByManagerId(@PathVariable Long managerId) {
        Long userId = getCurrentUserId();
        if (!hasRole(userId, "ROLE_ADMIN")) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var vehicles = vehicleQueryService.handle(new GetVehiclesByManagerIdQuery(managerId));
        if (vehicles.isEmpty()) { return ResponseEntity.noContent().build(); }
        var resources = VehicleResourceFromEntityAssembler.toResourceFromEntities(vehicles);
        return ResponseEntity.ok(resources);
    }


    /**
     * Update an existing vehicle.
     * @param vehicleId the ID of the vehicle to update
     * @param resource the UpdateVehicleResource containing the updated vehicle data.
     * @return ResponseEntity containing the updated VehicleResource if successful, or an error response.
     */
    @PutMapping(value = "/{vehicleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this vehicle")
    })
    public ResponseEntity<VehicleResource> updateVehicle(@PathVariable Long vehicleId, @Valid @RequestBody UpdateVehicleResource resource) {
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), null)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedVehicleOptional = vehicleCommandService.handle(vehicleId, command);
        return updatedVehicleOptional.map(value -> ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Assign a carrier to a vehicle.
     * @param vehicleId the ID of the vehicle to assign the carrier to
     * @param carrierId the ID of the carrier to assign to the vehicle
     * @return ResponseEntity containing the updated VehicleResource if successful, or an error response.
     */
    @PutMapping("/{vehicleId}/carrier/{carrierId}")
    @Operation(summary = "Assign a carrier to a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrier assigned to vehicle successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to assign carrier to this vehicle")
    })
    public ResponseEntity<VehicleResource> assignCarrierToVehicle(@PathVariable Long vehicleId, @PathVariable Long carrierId) {
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), null)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var updatedVehicleOptional = vehicleCommandService.handle(vehicleId, carrierId);
        if (updatedVehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(updatedVehicleOptional.get()));
    }

    /**
     * Change the status of a vehicle.
     * @param vehicleId the ID of the vehicle to change the status of
     * @param status the new status to set for the vehicle
     * @return ResponseEntity containing the updated VehicleResource if successful, or an error response.
     */
    @PutMapping("/{vehicleId}/status/{status}")
    @Operation(summary = "Change the status of a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle status changed successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to change the status of this vehicle")
    })
    public ResponseEntity<VehicleResource> changeVehicleStatus(@PathVariable Long vehicleId, @PathVariable VehicleStatus status) {
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), null)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var updatedVehicleOptional = vehicleCommandService.handle(vehicleId, status);
        if (updatedVehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(updatedVehicleOptional.get()));
    }

    /**
     * Remove the carrier assignment from a vehicle.
     * @param vehicleId the ID of the vehicle to remove the carrier from
     * @return ResponseEntity containing the updated VehicleResource if successful, or an error response.
     */
    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "Remove carrier assignment from a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrier removed from vehicle successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to remove carrier from this vehicle")
    })
    public ResponseEntity<VehicleResource> removeCarrierFromVehicle(@PathVariable Long vehicleId) {
        var vehicleOptional = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        var vehicle = vehicleOptional.get();
        if (!canAccessVehicle(vehicle.getManagerId(), null)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        var updatedVehicleOptional = vehicleCommandService.handle(vehicleId);
        if (updatedVehicleOptional.isEmpty()) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(updatedVehicleOptional.get()));

    }



    private Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { return 0L; }
        String email = authentication.getName();
        return iamContextFacade.fetchUserIdByEmail(email);
    }

    private boolean hasRole(Long userId, String role){
        var user = iamContextFacade.fetchUserById(userId);
        return user.isPresent() && user.get().roles().contains(role);
    }

    private boolean canAccessVehicle(Long vehicleManagerId, Long vehicleCarrierId) {
        Long userId = getCurrentUserId();
        if (userId == 0 ) { return false; }
        if (hasRole(userId, "ROLE_ADMIN")) { return true; }
        if (hasRole(userId, "ROLE_MANAGER") && vehicleManagerId.equals(userId)) { return true; }
        return hasRole(userId, "ROLE_CARRIER") && vehicleCarrierId != null && vehicleCarrierId.equals(userId);
    }
}
