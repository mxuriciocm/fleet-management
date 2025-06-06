package com.example.fleetmanagement.reports.application.internal.queryservices;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportByIdQuery;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportsByCarrierIdQuery;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportsByManagerIdQuery;
import com.example.fleetmanagement.reports.domain.model.queries.GetReportsByTypeQuery;
import com.example.fleetmanagement.reports.domain.services.ReportQueryService;
import com.example.fleetmanagement.reports.infrastructure.persistence.jpa.repositories.ReportRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReportQueryServiceImpl implements ReportQueryService {
    private final ReportRepository reportRepository;

    public ReportQueryServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Optional<Report> handle(GetReportByIdQuery query) {
        return reportRepository.findById(query.reportId());
    }

    @Override
    public List<Report> handle(GetReportsByCarrierIdQuery query) {
        return reportRepository.findByCarrierId(query.carrierId());
    }

    @Override
    public List<Report> handle(GetReportsByManagerIdQuery query) {
        return reportRepository.findByManagerId(query.managerId());
    }

    @Override
    public List<Report> handle(GetReportsByTypeQuery query) {
        return reportRepository.findByType(query.type());
    }
}
