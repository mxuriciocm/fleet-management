package com.example.fleetmanagement.reports.interfaces.rest.resources;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

import java.time.LocalDateTime;

public record ReportResource(Long id, String title, String content, ReportType type, LocalDateTime reportDate, Long carrierId, Long managerId, Long vehicleId, Long shipmentId) {}
