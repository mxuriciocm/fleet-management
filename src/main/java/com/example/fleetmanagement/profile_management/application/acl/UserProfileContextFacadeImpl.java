package com.example.fleetmanagement.profile_management.application.acl;

import com.example.fleetmanagement.profile_management.domain.model.commands.CreateUserProfileCommand;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileCommandService;
import com.example.fleetmanagement.profile_management.domain.services.UserProfileQueryService;
import com.example.fleetmanagement.profile_management.interfaces.acl.UserProfileContextFacade;
import org.springframework.stereotype.Service;

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

    public Long createUserProfile(String firstName, String lastName, String phoneNumber) {
        var createUserProfileCommand = new CreateUserProfileCommand(firstName, lastName, phoneNumber);
        var userProfile = userProfileCommandService.handle(createUserProfileCommand);
        return userProfile.isEmpty() ? Long.valueOf(0L) : userProfile.get().getId();
    }
}
