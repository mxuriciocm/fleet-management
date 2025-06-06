package com.example.fleetmanagement.reports.domain.services;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.domain.model.commands.CreateReportCommand;
import com.example.fleetmanagement.reports.domain.model.commands.UpdateReportCommand;

import java.util.Optional;

/**
 * Report Command Service Interface
 * This interface defines methods for handling commands related to reports.
 */
public interface ReportCommandService {

    /**
     * Handle Create Report Command
     * @param command the command to create a report
     * @return the created report
     */
    Report handle(CreateReportCommand command);

    /**
     * Handle Update Report Command
     * @param reportId the ID of the report to update
     * @param command the command to update the report
     * @return an Optional containing the updated report if successful, or empty if not found
     */
    Optional<Report> handle(Long reportId, UpdateReportCommand command);

    /**
     * Handle Delete Report Command
     * @param reportId the ID of the report to delete
     * @return true if the report was deleted successfully, false otherwise
     */
    boolean deleteReport(Long reportId);
}
