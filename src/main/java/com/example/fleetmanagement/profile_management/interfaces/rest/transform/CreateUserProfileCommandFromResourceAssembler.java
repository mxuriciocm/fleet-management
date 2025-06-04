package com.example.fleetmanagement.profile_management.interfaces.rest.transform;

import com.example.fleetmanagement.profile_management.domain.model.commands.CreateUserProfileCommand;
import com.example.fleetmanagement.profile_management.interfaces.rest.resources.CreateUserProfileResource;

public class CreateUserProfileCommandFromResourceAssembler {
    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.phoneNumber()
        );
    }
}
