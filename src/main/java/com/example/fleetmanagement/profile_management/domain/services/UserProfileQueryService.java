package com.example.fleetmanagement.profile_management.domain.services;

import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import com.example.fleetmanagement.profile_management.domain.model.queries.*;
import java.util.List;
import java.util.Optional;

/**
 * User profile query service.
 * <p>
 *     This service is responsible for handling user profile queries.
 *     It provides methods to handle queries for getting a user profile by ID, by email, and for getting all user profiles.
 * </p>
 */
public interface UserProfileQueryService {
    /**
     * Handle get user profile by ID query.
     *
     * @param query the query containing the user ID
     * @return an optional of UserProfile if found
     */
    Optional<UserProfile> handle(GetUserProfileByIdQuery query);

    /**
     * Handle get all user profiles queries.
     *
     * @param query the query to get all user profiles
     * @return a list of UserProfile
     */
    List<UserProfile> handle(GetAllUserProfilesQuery query);
}
