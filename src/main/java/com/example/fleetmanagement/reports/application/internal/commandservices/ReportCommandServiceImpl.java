package com.example.fleetmanagement.reports.application.internal.commandservices;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.domain.model.commands.CreateReportCommand;
import com.example.fleetmanagement.reports.domain.model.commands.UpdateReportCommand;
import com.example.fleetmanagement.reports.domain.services.ReportCommandService;
import com.example.fleetmanagement.reports.infrastructure.persistence.jpa.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportCommandServiceImpl implements ReportCommandService {
    private final ReportRepository reportRepository;

    public ReportCommandServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report handle(CreateReportCommand command) {
        var report = new Report(command);
        return reportRepository.save(report);
    }

    @Override
    public Optional<Report> handle(Long reportId, UpdateReportCommand command) {
        return reportRepository.findById(reportId)
                .map(report -> {
                    if (command.title() != null) { report.updateTitle(command.title()); }
                    if (command.content() != null) { report.updateContent(command.content()); }
                    if (command.type() != null) { report.updateType(command.type()); }
                    return reportRepository.save(report);
                });
    }

    @Override
    public boolean deleteReport(Long reportId){
        if (!reportRepository.existsById(reportId)) { return false; }
        try {
            reportRepository.deleteById(reportId);
            return true;
        } catch (Exception e) {
            // Log the exception if necessary
            return false;
        }
    }

}
