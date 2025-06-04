package com.example.fleetmanagement.profile.application.internal.queryservices;

import com.example.fleetmanagement.profile.domain.model.aggregates.Profile;
import com.example.fleetmanagement.profile.domain.model.queries.GetAllProfilesQuery;
import com.example.fleetmanagement.profile.domain.model.queries.GetProfileByIdQuery;
import com.example.fleetmanagement.profile.domain.services.ProfileQueryService;
import com.example.fleetmanagement.profile.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository userProfileRepository;

    public ProfileQueryServiceImpl(ProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query){
        System.out.println("Searching for profile with userId: " + query.userId()); // Log para debug
        var profile = userProfileRepository.findByUserId(query.userId());
        System.out.println("Profile found: " + (profile.isPresent() ? "yes" : "no")); // Log para debug
        return profile;
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query){
        var profiles = userProfileRepository.findAll();
        System.out.println("Found " + profiles.size() + " profiles"); // Log para debug
        return profiles;
    }
}
