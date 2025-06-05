package com.example.fleetmanagement.profile.interfaces.rest.transform;

import com.example.fleetmanagement.profile.domain.model.commands.UpdateProfileCommand;
import com.example.fleetmanagement.profile.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {

    public static UpdateProfileCommand toCommandFromResource(UpdateProfileResource resource, Long userId) {
        return new UpdateProfileCommand(userId, resource.firstName(), resource.lastName(), resource.phoneNumber());
    }
}
