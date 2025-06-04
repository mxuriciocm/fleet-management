package com.example.fleetmanagement.profile.interfaces.rest.transform;

import com.example.fleetmanagement.profile.domain.model.commands.UpdateProfileCommand;
import com.example.fleetmanagement.profile.interfaces.rest.resources.UpdateProfileResource;

/**
 * Clase para transformar el recurso de actualización de perfil en un comando de dominio
 */
public class UpdateProfileCommandFromResourceAssembler {

    public static UpdateProfileCommand toCommandFromResource(UpdateProfileResource resource, Long userId) {
        return new UpdateProfileCommand(
            userId,
            resource.firstName(),
            resource.lastName(),
            resource.phoneNumber()
        );
    }
}
