package com.example.fleetmanagement.shipments.interfaces.rest.transform;

import com.example.fleetmanagement.shipments.domain.model.commands.UpdateShipmentCommand;
import com.example.fleetmanagement.shipments.interfaces.rest.resources.UpdateShipmentResource;

public class UpdateShipmentCommandFromResourceAssembler {
    public static UpdateShipmentCommand toCommandFromResource(UpdateShipmentResource resource) {
        return new UpdateShipmentCommand(resource.destination(), resource.description(), resource.scheduledDate());
    }
}
