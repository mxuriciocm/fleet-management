package com.example.fleetmanagement.profile_management.interfaces.rest.transform;

import com.example.fleetmanagement.iam.interfaces.acl.dto.UserDto;
import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import com.example.fleetmanagement.profile_management.interfaces.rest.resources.UserProfileWithUserResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserProfileWithUserResourceAssembler {

    public static UserProfileWithUserResource toResourceFromEntities(UserProfile profile, Optional<UserDto> userDto) {
        String email = userDto.map(UserDto::email).orElse("");
        List<String> roles = userDto.map(UserDto::roles).orElse(Collections.emptyList());

        return new UserProfileWithUserResource(
            profile.getId(),
            profile.getFullName(),
            profile.getPhoneNumber(),
            email,
            roles
        );
    }
}
