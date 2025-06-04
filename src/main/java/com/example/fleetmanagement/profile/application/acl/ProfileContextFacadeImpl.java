package com.example.fleetmanagement.profile.application.acl;

import com.example.fleetmanagement.profile.domain.model.commands.CreateProfileCommand;
import com.example.fleetmanagement.profile.domain.model.queries.GetProfileByIdQuery;
import com.example.fleetmanagement.profile.domain.services.ProfileCommandService;
import com.example.fleetmanagement.profile.domain.services.ProfileQueryService;
import com.example.fleetmanagement.profile.interfaces.acl.ProfileContextFacade;
import com.example.fleetmanagement.profile.interfaces.acl.dto.ProfileDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserProfile Context Facade Implementation
 */
@Service
public class ProfileContextFacadeImpl implements ProfileContextFacade {
    private final ProfileCommandService userProfileCommandService;
    private final ProfileQueryService userProfileQueryService;

    public ProfileContextFacadeImpl(ProfileCommandService userProfileCommandService, ProfileQueryService userProfileQueryService) {
        this.userProfileCommandService = userProfileCommandService;
        this.userProfileQueryService = userProfileQueryService;
    }

    @Override
    public Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber) {
        var createUserProfileCommand = new CreateProfileCommand(userId, firstName, lastName, phoneNumber);
        var userProfile = userProfileCommandService.handle(createUserProfileCommand);
        return userProfile.isEmpty() ? 0L : userProfile.get().getId();
    }

    @Override
    public Optional<ProfileDto> fetchProfileByUserId(Long userId) {
        var query = new GetProfileByIdQuery(userId);
        var userProfile = userProfileQueryService.handle(query);

        return userProfile.map(profile -> new ProfileDto(
            profile.getUserId(),
            profile.getFullName(),
            profile.getPhoneNumber()
        ));
    }
}
