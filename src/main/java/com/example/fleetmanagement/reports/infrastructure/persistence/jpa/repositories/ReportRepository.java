package com.example.fleetmanagement.reports.infrastructure.persistence.jpa.repositories;

import com.example.fleetmanagement.reports.domain.model.aggregates.Report;
import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Find reports by carrier ID.
     * @param carrierId the ID of the carrier
     * @return a List of Reports associated with the specified carrier ID
     */
    List<Report> findByCarrierId(Long carrierId);

    /**
     * Find reports by manager ID.
     * @param managerId the ID of the manager
     * @return a List of Reports associated with the specified manager ID
     */
    List<Report> findByManagerId(Long managerId);

    /**
     * Find reports by type.
     * @param type the type of report
     * @return a List of Reports of the specified type
     */
    List<Report> findByType(ReportType type);

    /**
     * Find reports by carrier ID and type.
     * @param carrierId the ID of the carrier
     * @param type the type of report
     * @return a List of Reports associated with the specified carrier ID and type
     */
    List<Report> findByCarrierIdAndType(Long carrierId, ReportType type);

    /**
     * Find reports by manager ID and type.
     * @param managerId the ID of the manager
     * @param type the type of report
     * @return a List of Reports associated with the specified manager ID and type
     */
    List<Report> findByManagerIdAndType(Long managerId, ReportType type);
}
