package com.example.fleetmanagement.profile.interfaces.rest;

import com.example.fleetmanagement.iam.interfaces.acl.IamContextFacade;
import com.example.fleetmanagement.profile.domain.model.queries.GetAllProfilesQuery;
import com.example.fleetmanagement.profile.domain.model.queries.GetProfileByIdQuery;
import com.example.fleetmanagement.profile.domain.services.ProfileQueryService;
import com.example.fleetmanagement.profile.domain.services.ProfileCommandService;
import com.example.fleetmanagement.profile.interfaces.rest.resources.ProfileResource;
import com.example.fleetmanagement.profile.interfaces.rest.resources.ProfileWithUserResource;
import com.example.fleetmanagement.profile.interfaces.rest.resources.UpdateProfileResource;
import com.example.fleetmanagement.profile.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.example.fleetmanagement.profile.interfaces.rest.transform.ProfileWithUserResourceAssembler;
import com.example.fleetmanagement.profile.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {
    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;
    private final IamContextFacade iamContextFacade;

    public ProfilesController(ProfileQueryService profileQueryService,
                             ProfileCommandService profileCommandService,
                             IamContextFacade iamContextFacade) {
        this.profileQueryService = profileQueryService;
        this.profileCommandService = profileCommandService;
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Get a profile by userId with user data (email and roles)
     * @param userId the ID of the user whose profile is to be retrieved
     * @return ResponseEntity containing the ProfileWithUserResource or an error response
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get a profile by userId with user data (email and roles)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found with user data"),
            @ApiResponse(responseCode = "404", description = "Profile not found")})
    public ResponseEntity<ProfileWithUserResource> getProfileById(@PathVariable Long userId) {
        var query = new GetProfileByIdQuery(userId);
        var profile = profileQueryService.handle(query);
        if (profile.isEmpty()) { return ResponseEntity.notFound().build(); }
        var userData = iamContextFacade.fetchUserById(userId);
        var resource = ProfileWithUserResourceAssembler.toResourceFromEntities(profile.get(), userData);
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all profiles with user data (email and roles)
     * @return List of ProfileWithUserResource
     */
    @GetMapping
    @Operation(summary = "Get all profiles with user data (email and roles)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles found with user data"),
            @ApiResponse(responseCode = "204", description = "No profiles found")})
    public ResponseEntity<List<ProfileWithUserResource>> getAllProfiles() {
        var query = new GetAllProfilesQuery();
        var profiles = profileQueryService.handle(query);
        if (profiles.isEmpty()) { return ResponseEntity.noContent().build();}
        var profilesWithUserData = profiles.stream()
            .map(profile -> {
                var userData = iamContextFacade.fetchUserById(profile.getUserId());
                return ProfileWithUserResourceAssembler.toResourceFromEntities(profile, userData);
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(profilesWithUserData);
    }

    /**
     * Update a profile by userId
     * @param userId the ID of the user whose profile is to be updated
     * @param resource the resource containing the updated profile data
     * @return ResponseEntity containing the updated ProfileResource or an error response
     */

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid profile data")})
    public ResponseEntity<ProfileResource> updateProfile(@PathVariable Long userId, @RequestBody UpdateProfileResource resource) {
        var query = new GetProfileByIdQuery(userId);
        var existingProfile = profileQueryService.handle(query);
        if (existingProfile.isEmpty()) { return ResponseEntity.notFound().build(); }
        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var updatedProfile = profileCommandService.handle(command);
        if (updatedProfile.isEmpty()) { return ResponseEntity.badRequest().build(); }
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile.get());
        return ResponseEntity.ok(profileResource);
    }
}
