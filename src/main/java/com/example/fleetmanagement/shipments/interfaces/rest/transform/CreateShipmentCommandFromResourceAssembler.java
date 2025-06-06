package com.example.fleetmanagement.shipments.interfaces.rest.transform;

import com.example.fleetmanagement.shipments.domain.model.commands.CreateShipmentCommand;
import com.example.fleetmanagement.shipments.interfaces.rest.resources.CreateShipmentResource;

public class CreateShipmentCommandFromResourceAssembler {
    public static CreateShipmentCommand toCommandFromResource(CreateShipmentResource resource, Long managerId) {
        return new CreateShipmentCommand(resource.destination(), resource.description(), resource.scheduledDate(), managerId);
    }
}
