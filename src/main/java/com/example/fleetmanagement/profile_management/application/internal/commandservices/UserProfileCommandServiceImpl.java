package com.example.fleetmanagement.profile_management.application.internal.commandservices;

import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import com.example.fleetmanagement.profile_management.domain.model.commands.CreateUserProfileCommand;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileCommandService;
import com.example.fleetmanagement.profile_management.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileCommandServiceImpl implements UserProfileCommandService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileCommandServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> handle(CreateUserProfileCommand command) {
        System.out.println("Creating profile for userId: " + command.userId()); // Log para debug
        var userProfile = new UserProfile(command);
        var savedProfile = userProfileRepository.save(userProfile);
        System.out.println("Profile saved with ID: " + savedProfile.getId() + " for userId: " + savedProfile.getUserId()); // Log para debug
        return Optional.of(savedProfile);
    }
}
