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
        var userProfile = new UserProfile(command);
        userProfileRepository.save(userProfile);
        return Optional.of(userProfile);
    }
}
