package com.example.fleetmanagement.profile.application.internal.commandservices;

import com.example.fleetmanagement.profile.domain.model.aggregates.Profile;
import com.example.fleetmanagement.profile.domain.model.commands.CreateProfileCommand;
import com.example.fleetmanagement.profile.domain.services.ProfileCommandService;
import com.example.fleetmanagement.profile.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository userProfileRepository;

    public ProfileCommandServiceImpl(ProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {
        System.out.println("Creating profile for userId: " + command.userId()); // Log para debug
        var userProfile = new Profile(command);
        var savedProfile = userProfileRepository.save(userProfile);
        System.out.println("Profile saved with ID: " + savedProfile.getId() + " for userId: " + savedProfile.getUserId()); // Log para debug
        return Optional.of(savedProfile);
    }
}
