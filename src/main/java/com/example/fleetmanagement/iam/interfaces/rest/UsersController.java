package com.example.fleetmanagement.iam.interfaces.rest;

import com.example.fleetmanagement.iam.domain.model.queries.GetAllUsersQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByIdQuery;
import com.example.fleetmanagement.iam.domain.services.UserQueryService;
import com.example.fleetmanagement.iam.interfaces.rest.resources.UserWithProfileResource;
import com.example.fleetmanagement.iam.interfaces.rest.transform.UserWithProfileResourceFromEntityAssembler;
import com.example.fleetmanagement.profile_management.interfaces.acl.UserProfileContextFacade;
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

/**
 * Controller to handle user endpoints.
 * <p>
 *     This class is used to handle user endpoints.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;
    private final UserProfileContextFacade userProfileContextFacade;

    /**
     * Constructor.
     *
     * @param userQueryService The user query service.
     * @param userProfileContextFacade The user profile context facade.
     */
    public UsersController(UserQueryService userQueryService, UserProfileContextFacade userProfileContextFacade) {
        this.userQueryService = userQueryService;
        this.userProfileContextFacade = userProfileContextFacade;
    }

    /**
     * Get all users with their profile data.
     *
     * @return The list of users with their profile data.
     */
    @GetMapping
    @Operation(summary = "Get all users",
              description = "Get all users with their associated profile information (name, phone, etc).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully."),
            @ApiResponse(responseCode = "204", description = "No users found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<List<UserWithProfileResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var usersWithProfile = users.stream()
            .map(user -> {
                var profileData = userProfileContextFacade.fetchProfileByUserId(user.getId());
                return UserWithProfileResourceFromEntityAssembler.toResourceFromEntity(user, profileData);
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(usersWithProfile);
    }

    /**
     * Get user by id.
     *
     * @param userId The id of the user to retrieve.
     * @return The user with their profile data.
     */
    @GetMapping(value = "/{userId}")
    @Operation(summary = "Get user by id",
              description = "Get the user with the given id including their profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<UserWithProfileResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileData = userProfileContextFacade.fetchProfileByUserId(userId);

        var userWithProfileResource = UserWithProfileResourceFromEntityAssembler
            .toResourceFromEntity(user.get(), profileData);

        return ResponseEntity.ok(userWithProfileResource);
    }
}
