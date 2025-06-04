package com.example.fleetmanagement.profile_management.domain.services;

import com.example.fleetmanagement.profile_management.domain.model.commands.*;
import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import java.util.Optional;

/**
 * UserProfile command service
 */
public interface UserProfileCommandService {
    /**
     * Handle Create UserProfile Command
     *
     * @param command The {@link CreateUserProfileCommand} Command
     * @return An {@link Optional<UserProfile>} instance if the command is valid, otherwise empty
     * @throws IllegalArgumentException if the email address already exists
     */
    Optional<UserProfile> handle(CreateUserProfileCommand command);
}
