package com.example.fleetmanagement.profile.interfaces.rest.transform;

import com.example.fleetmanagement.profile.domain.model.aggregates.Profile;
import com.example.fleetmanagement.profile.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(entity.getUserId(), entity.getFullName(), entity.getPhoneNumber());
    }
}
