package com.example.fleetmanagement.profile_management.interfaces.rest;

import com.example.fleetmanagement.profile_management.domain.model.queries.GetAllUserProfilesQuery;
import com.example.fleetmanagement.profile_management.domain.model.queries.GetUserProfileByIdQuery;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileCommandService;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileQueryService;
import com.example.fleetmanagement.profile_management.interfaces.rest.resources.CreateUserProfileResource;
import com.example.fleetmanagement.profile_management.interfaces.rest.resources.UserProfileResource;
import com.example.fleetmanagement.profile_management.interfaces.rest.transform.CreateUserProfileCommandFromResourceAssembler;
import com.example.fleetmanagement.profile_management.interfaces.rest.transform.UserProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Profiles", description = "Available User Profile Endpoints")
public class UserProfileController {
    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;

    public UserProfileController(UserProfileCommandService userProfileCommandService, UserProfileQueryService userProfileQueryService) {
        this.userProfileCommandService = userProfileCommandService;
        this.userProfileQueryService = userProfileQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User profile created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<UserProfileResource> createUserProfile(@RequestBody CreateUserProfileResource resource) {
        var createUserProfileCommand = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var userProfile = userProfileCommandService.handle(createUserProfileCommand);
        if (userProfile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var createdUserProfile = userProfile.get();
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(createdUserProfile);
        return ResponseEntity.ok(userProfileResource);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")})
    public ResponseEntity<UserProfileResource> getProfileById(@PathVariable Long userId) {
        var getProfileByIdQuery = new GetUserProfileByIdQuery(userId);
        var userProfile = userProfileQueryService.handle(getProfileByIdQuery);
        if (userProfile.isEmpty()) return ResponseEntity.notFound().build();
        var userProfileEntity = userProfile.get();
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfileEntity);
        return ResponseEntity.ok(userProfileResource);
    }

    @GetMapping
    @Operation(summary = "Get all profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles found"),
            @ApiResponse(responseCode = "404", description = "Profiles not found")})
    public ResponseEntity<List<UserProfileResource>> getAllProfiles() {
        var profiles = userProfileQueryService.handle(new GetAllUserProfilesQuery());
        if (profiles.isEmpty()) return ResponseEntity.notFound().build();
        var profileResources = profiles.stream()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profileResources);
    }
}
