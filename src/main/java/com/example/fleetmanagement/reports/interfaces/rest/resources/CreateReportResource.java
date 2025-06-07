package com.example.fleetmanagement.reports.interfaces.rest.resources;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

public record CreateReportResource(String title, String content, ReportType type, Long vehicleId, Long shipmentId) {
    public CreateReportResource {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }
    }
}
