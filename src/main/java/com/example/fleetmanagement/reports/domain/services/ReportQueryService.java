package com.example.fleetmanagement.reports.domain.services;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.domain.model.queries.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportQueryService {

    /**
     * Handle Get Report by ID Query
     * @param query the query containing the report ID
     * @return an Optional containing the Report if found, or empty if not found
     */
    Optional<Report> handle(GetReportByIdQuery query);

    /**
     * Handle Get Reports carrier ID Query
     * @param query the query containing the criteria
     * @return a List of Reports associated with the specified carrier ID
     */
    List<Report> handle(GetReportsByCarrierIdQuery query);

    /**
     * Handle Get Reports by Manager ID Query
     * @param query the query containing the manager ID
     * @return a List of Reports associated with the specified manager ID
     */
    List<Report> handle(GetReportsByManagerIdQuery query);

    /**
     * Handle Get Reports by Type Query
     * @param query the query containing the report type
     * @return a List of Reports of the specified type
     */
    List<Report> handle(GetReportsByTypeQuery query);

    /**
     * Handle Get Reports by Filter Query
     * @param query the query containing the filter criteria
     * @return a List of Reports that match the filter criteria
     */
    List<Report> handle(GetReportsByFilterQuery query);

    /**
     * Find reports by date range
     * @param startDate date to start the range from
     * @param endDate date to end the range at
     * @return a List of Reports within the specified date range
     */
    List<Report> findReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find reports by type and date range
     * @param type the type of report to filter by
     * @param startDate date to start the range from
     * @param endDate date to end the range at
     * @return a List of Reports of the specified type within the specified date range
     */
    List<Report> findReportsByTypeAndDateRange(String type, LocalDateTime startDate, LocalDateTime endDate);

}
