package com.example.fleetmanagement.profile.interfaces.acl;

import com.example.fleetmanagement.profile.interfaces.acl.dto.ProfileDto;
import java.util.Optional;

/**
 * UserProfileContextFacade
 */
public interface ProfileContextFacade {
    Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber);

    Optional<ProfileDto> fetchProfileByUserId(Long userId);
}
