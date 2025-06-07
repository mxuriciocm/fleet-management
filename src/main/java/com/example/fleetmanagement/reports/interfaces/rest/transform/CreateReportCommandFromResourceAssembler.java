package com.example.fleetmanagement.reports.interfaces.rest.transform;

import com.example.fleetmanagement.reports.domain.model.commands.CreateReportCommand;
import com.example.fleetmanagement.reports.interfaces.rest.resources.CreateReportResource;

import java.time.LocalDateTime;

public class CreateReportCommandFromResourceAssembler {
    public static CreateReportCommand toCommandFromResource(CreateReportResource resource, Long carrierId, Long managerId) {
        return new CreateReportCommand(resource.title(),
                resource.content(),
                resource.type(),
                LocalDateTime.now(),
                carrierId,
                managerId,
                resource.vehicleId(),
                resource.shipmentId());
    }
}

