package com.example.fleetmanagement.iam.interfaces.rest.transform;

import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import com.example.fleetmanagement.iam.interfaces.rest.resources.UserWithProfileResource;
import com.example.fleetmanagement.profile_management.interfaces.acl.dto.UserProfileDto;

import java.util.Optional;
import java.util.stream.Collectors;

public class UserWithProfileResourceFromEntityAssembler {

    public static UserWithProfileResource toResourceFromEntity(User entity, Optional<UserProfileDto> profileDto) {
        var roles = entity.getRoles().stream()
                .map(role -> role.getStringName())
                .collect(Collectors.toList());

        String fullName = profileDto.map(UserProfileDto::fullName).orElse("");
        String phoneNumber = profileDto.map(UserProfileDto::phoneNumber).orElse("");

        return new UserWithProfileResource(entity.getId(), entity.getEmail(), roles, fullName, phoneNumber);
    }
}
