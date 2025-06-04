package com.example.fleetmanagement.profile.domain.services;

import com.example.fleetmanagement.profile.domain.model.commands.*;
import com.example.fleetmanagement.profile.domain.model.aggregates.Profile;
import java.util.Optional;

/**
 * UserProfile command service
 */
public interface ProfileCommandService {
    /**
     * Handle Create UserProfile Command
     *
     * @param command The {@link CreateProfileCommand} Command
     * @return An {@link Optional< Profile >} instance if the command is valid, otherwise empty
     * @throws IllegalArgumentException if the email address already exists
     */
    Optional<Profile> handle(CreateProfileCommand command);
}
