package com.example.fleetmanagement.profile_management.domain.services;

import com.example.fleetmanagement.profile_management.domain.model.commands.*;
import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import java.util.Optional;

/**
 * UserProfile command service
 * <p>
 *     This service is responsible for handling commands related to user profiles.
 *     It provides methods to create and update user profiles,
 * </p>
 */
public interface UserProfileCommandService {

    /**
     * Handle command to create a new user profile.
     *
     * @param command the command containing user profile data
     * @return an Optional containing the created UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(CreateUserProfileCommand command);

    /**
     * Handle command to update an existing user profile.
     *
     * @param command the command containing updated user profile data
     * @return an Optional containing the updated UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(UpdateUserProfileFullNameCommand command);

    /**
     * Handle command to update the phone number of a user profile.
     *
     * @param command the command containing the new phone number
     * @return an Optional containing the updated UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(UpdateUserProfilePhoneCommand command);

    /**
     * Handle command to update the emergency contact of a user profile.
     *
     * @param command the command containing the new emergency contact details
     * @return an Optional containing the updated UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(UpdateUserProfileEmergencyContactCommand command);

    /**
     * Handle command to update the email of a user profile.
     *
     * @param command the command containing the new email
     * @return an Optional containing the updated UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(UpdateProfileEmailCommand command);

    /**
     * Handle command to update the role of a user profile.
     *
     * @param command the command containing the new role
     * @return an Optional containing the updated UserProfile if successful, otherwise empty
     */
    Optional<UserProfile> handle(UpdateUserProfileRoleCommand command);
}
