package com.example.fleetmanagement.reports.interfaces.rest.transform;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.interfaces.rest.resources.ReportResource;

import java.util.List;

public class ReportResourceFromEntityAssembler {
    public static ReportResource toResourceFromEntity(Report entity){
        return new ReportResource(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getType(),
                entity.getReportDate(),
                entity.getCarrierId(),
                entity.getManagerId(),
                entity.getVehicleId(),
                entity.getShipmentId()
        );
    }

    public static List<ReportResource> toResourceFromEntities(List<Report> entities){
        return entities.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
