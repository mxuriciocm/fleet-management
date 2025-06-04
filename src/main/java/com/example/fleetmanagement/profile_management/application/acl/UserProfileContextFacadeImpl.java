package com.example.fleetmanagement.profile_management.application.acl;

import com.example.fleetmanagement.profile_management.domain.model.commands.CreateUserProfileCommand;
import com.example.fleetmanagement.profile_management.domain.model.queries.GetUserProfileByIdQuery;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileCommandService;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileQueryService;
import com.example.fleetmanagement.profile_management.interfaces.acl.UserProfileContextFacade;
import com.example.fleetmanagement.profile_management.interfaces.acl.dto.UserProfileDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserProfile Context Facade Implementation
 */
@Service
public class UserProfileContextFacadeImpl implements UserProfileContextFacade {
    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;

    public UserProfileContextFacadeImpl(UserProfileCommandService userProfileCommandService, UserProfileQueryService userProfileQueryService) {
        this.userProfileCommandService = userProfileCommandService;
        this.userProfileQueryService = userProfileQueryService;
    }

    @Override
    public Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber) {
        var createUserProfileCommand = new CreateUserProfileCommand(userId, firstName, lastName, phoneNumber);
        var userProfile = userProfileCommandService.handle(createUserProfileCommand);
        return userProfile.isEmpty() ? 0L : userProfile.get().getId();
    }

    @Override
    public Optional<UserProfileDto> fetchProfileByUserId(Long userId) {
        var query = new GetUserProfileByIdQuery(userId);
        var userProfile = userProfileQueryService.handle(query);

        return userProfile.map(profile -> new UserProfileDto(
            profile.getUserId(),
            profile.getFullName(),
            profile.getPhoneNumber()
        ));
    }
}
