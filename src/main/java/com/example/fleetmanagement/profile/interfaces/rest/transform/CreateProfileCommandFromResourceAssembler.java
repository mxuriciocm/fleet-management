package com.example.fleetmanagement.profile.interfaces.rest.transform;

import com.example.fleetmanagement.profile.domain.model.commands.CreateProfileCommand;
import com.example.fleetmanagement.profile.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.phoneNumber()
        );
    }
}
