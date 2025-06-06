package com.example.fleetmanagement.reports.domain.model.queries;

import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;

import java.time.LocalDateTime;

public record GetReportsByFilterQuery(Long carrierId, Long managerId, ReportType type, LocalDateTime startDate, LocalDateTime endDate) {
}
