package com.example.fleetmanagement.profile_management.application.internal.queryservices;

import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import com.example.fleetmanagement.profile_management.domain.model.queries.GetAllUserProfilesQuery;
import com.example.fleetmanagement.profile_management.domain.model.queries.GetUserProfileByIdQuery;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileQueryService;
import com.example.fleetmanagement.profile_management.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileQueryServiceImpl implements UserProfileQueryService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileQueryServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> handle(GetUserProfileByIdQuery query){
        System.out.println("Searching for profile with userId: " + query.userId()); // Log para debug
        var profile = userProfileRepository.findByUserId(query.userId());
        System.out.println("Profile found: " + (profile.isPresent() ? "yes" : "no")); // Log para debug
        return profile;
    }

    @Override
    public List<UserProfile> handle(GetAllUserProfilesQuery query){
        var profiles = userProfileRepository.findAll();
        System.out.println("Found " + profiles.size() + " profiles"); // Log para debug
        return profiles;
    }
}
