package com.example.fleetmanagement.profile_management.interfaces.rest.transform;

import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import com.example.fleetmanagement.profile_management.interfaces.rest.resources.UserProfileResource;

public class UserProfileResourceFromEntityAssembler {
    public static UserProfileResource toResourceFromEntity(UserProfile entity) {
        return new UserProfileResource(entity.getId(), entity.getFullName(), entity.getPhoneNumber());
    }
}
