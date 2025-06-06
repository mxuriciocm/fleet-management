package com.example.fleetmanagement.reports.domain.model.commands;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

public record UpdateReportCommand(String title, String content, ReportType type) {}
