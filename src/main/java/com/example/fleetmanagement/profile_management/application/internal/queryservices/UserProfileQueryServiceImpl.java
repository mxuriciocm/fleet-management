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
        return userProfileRepository.findById(query.userId());
    }

    @Override
    public List<UserProfile> handle(GetAllUserProfilesQuery query){
        return userProfileRepository.findAll();
    }
}
