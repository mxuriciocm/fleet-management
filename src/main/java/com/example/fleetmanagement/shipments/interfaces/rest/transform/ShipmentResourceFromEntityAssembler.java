package com.example.fleetmanagement.shipments.interfaces.rest.transform;

import com.example.fleetmanagement.shipments.domain.model.aggregates.Shipment;
import com.example.fleetmanagement.shipments.interfaces.rest.resources.ShipmentResource;

import java.util.List;
import java.util.stream.Collectors;

public class ShipmentResourceFromEntityAssembler {
    public static ShipmentResource toResourceFromEntity(Shipment entity) {
        return new ShipmentResource(
                entity.getId(),
                entity.getDestination(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getScheduledDate(),
                entity.getStartedDate(),
                entity.getCompletedDate(),
                entity.getManagerId(),
                entity.getCarrierId()
        );
    }

    public static List<ShipmentResource> toResourceFromEntities(List<Shipment> entities) {
        return entities.stream()
                .map(ShipmentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
