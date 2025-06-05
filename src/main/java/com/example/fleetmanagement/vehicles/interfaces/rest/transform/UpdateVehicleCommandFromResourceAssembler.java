package com.example.fleetmanagement.vehicles.interfaces.rest.transform;

import com.example.fleetmanagement.vehicles.domain.model.commands.UpdateVehicleCommand;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.UpdateVehicleResource;

public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(resource.brand(), resource.model(), resource.status());
    }
}
