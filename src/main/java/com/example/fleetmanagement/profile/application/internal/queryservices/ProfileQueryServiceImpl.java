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
        return userProfileRepository.findByUserId(query.userId());
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query){
        return userProfileRepository.findAll();
    }
}
