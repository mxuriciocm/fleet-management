package com.example.fleetmanagement.vehicles.interfaces.rest.transform;

import com.example.fleetmanagement.vehicles.domain.model.commands.CreateVehicleCommand;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.CreateVehicleResource;

public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource resource, Long managerId) {
        return new CreateVehicleCommand(resource.licensePlate(), resource.brand(), resource.model(), managerId);
    }
}
