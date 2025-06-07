package com.example.fleetmanagement.reports.interfaces.rest.transform;

import com.example.fleetmanagement.reports.domain.model.commands.UpdateReportCommand;
import com.example.fleetmanagement.reports.interfaces.rest.resources.UpdateReportResource;

public class UpdateReportCommandFromResourceAssembler {
    public static UpdateReportCommand toCommandFromResource(UpdateReportResource resource) {
        return new UpdateReportCommand(resource.title(), resource.content(), resource.type());
    }
}
