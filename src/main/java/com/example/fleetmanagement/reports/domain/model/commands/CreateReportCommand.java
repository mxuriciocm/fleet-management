package com.example.fleetmanagement.reports.domain.model.commands;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

import java.time.LocalDateTime;

public record CreateReportCommand(String title, String content, ReportType type, LocalDateTime reportDate, Long carrierId, Long managerId, Long vehicleId, Long shipmentId) {}
