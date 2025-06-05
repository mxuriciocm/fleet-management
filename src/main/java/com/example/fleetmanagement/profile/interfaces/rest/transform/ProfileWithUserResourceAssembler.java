package com.example.fleetmanagement.profile.interfaces.rest.transform;

import com.example.fleetmanagement.iam.interfaces.acl.dto.UserDto;
import com.example.fleetmanagement.profile.domain.model.aggregates.Profile;
import com.example.fleetmanagement.profile.interfaces.rest.resources.ProfileWithUserResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProfileWithUserResourceAssembler {

    public static ProfileWithUserResource toResourceFromEntities(Profile profile, Optional<UserDto> userDto) {
        String email = userDto.map(UserDto::email).orElse("");
        List<String> roles = userDto.map(UserDto::roles).orElse(Collections.emptyList());

        return new ProfileWithUserResource(profile.getId(), profile.getFullName(), profile.getPhoneNumber(), email, roles);
    }
}
