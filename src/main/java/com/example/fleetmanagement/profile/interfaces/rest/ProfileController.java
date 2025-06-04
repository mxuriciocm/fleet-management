package com.example.fleetmanagement.profile.interfaces.rest;

import com.example.fleetmanagement.iam.interfaces.acl.IamContextFacade;
import com.example.fleetmanagement.profile.domain.model.queries.GetAllProfilesQuery;
import com.example.fleetmanagement.profile.domain.model.queries.GetProfileByIdQuery;
import com.example.fleetmanagement.profile.domain.services.ProfileQueryService;
import com.example.fleetmanagement.profile.interfaces.rest.resources.ProfileWithUserResource;
import com.example.fleetmanagement.profile.interfaces.rest.transform.ProfileWithUserResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfileController {
    private final ProfileQueryService userProfileQueryService;
    private final IamContextFacade iamContextFacade;

    public ProfileController(ProfileQueryService userProfileQueryService, IamContextFacade iamContextFacade) {
        this.userProfileQueryService = userProfileQueryService;
        this.iamContextFacade = iamContextFacade;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a profile by userId with user data (email and roles)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found with user data"),
            @ApiResponse(responseCode = "404", description = "Profile not found")})
    public ResponseEntity<ProfileWithUserResource> getUserProfileById(@PathVariable Long userId) {
        var query = new GetProfileByIdQuery(userId);
        var userProfile = userProfileQueryService.handle(query);

        if (userProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userData = iamContextFacade.fetchUserById(userId);

        var resource = ProfileWithUserResourceAssembler.toResourceFromEntities(userProfile.get(), userData);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Get all profiles with user data (email and roles)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles found with user data"),
            @ApiResponse(responseCode = "204", description = "No profiles found")})
    public ResponseEntity<List<ProfileWithUserResource>> getAllUserProfiles() {
        var query = new GetAllProfilesQuery();
        var profiles = userProfileQueryService.handle(query);

        if (profiles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var profilesWithUserData = profiles.stream()
            .map(profile -> {
                var userData = iamContextFacade.fetchUserById(profile.getUserId());
                return ProfileWithUserResourceAssembler.toResourceFromEntities(profile, userData);
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(profilesWithUserData);
    }
}
