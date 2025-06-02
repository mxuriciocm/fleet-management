package com.example.fleetmanagement.iam.interfaces.rest.transform;

import com.example.fleetmanagement.iam.domain.model.commands.SignInCommand;
import com.example.fleetmanagement.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler to convert a SignInResource to a SignInCommand.
 * <p>
 *     This class is used to convert a SignInResource to a SignInCommand.
 * </p>
 */
public class SignInCommandFromResourceAssembler {
    /**
     * Converts a SignInResource to a SignInCommand.
     *
     * @param resource The SignInResource to convert.
     * @return The SignInCommand.
     */
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(
                resource.username(),
                resource.password());
    }
}
