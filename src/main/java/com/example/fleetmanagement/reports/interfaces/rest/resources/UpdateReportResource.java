package com.example.fleetmanagement.reports.interfaces.rest.resources;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

public record UpdateReportResource(String title, String content, ReportType type) {}
