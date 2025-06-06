package com.example.fleetmanagement.reports.domain.model.aggregates;

import com.example.fleetmanagement.reports.domain.model.commands.CreateReportCommand;
import com.example.fleetmanagement.reports.domain.model.valueobjects.ReportType;
import com.example.fleetmanagement.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Report extends AuditableAbstractAggregateRoot<Report> {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2000)
    @Column(length=2000)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @NotNull
    private LocalDateTime reportDate;

    @NotNull
    private Long carrierId;

    private Long managerId;

    private Long vehicleId;

    private Long shipmentId;

    public Report(String title, String content, ReportType type, LocalDateTime reportDate, Long carrierId, Long managerId, Long vehicleId, Long shipmentId) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.reportDate = reportDate;
        this.carrierId = carrierId;
        this.managerId = managerId;
        this.vehicleId = vehicleId;
        this.shipmentId = shipmentId;
    }

    public Report() {}

    public Report(CreateReportCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.type = command.type();
        this.reportDate = command.reportDate() != null ? command.reportDate() : LocalDateTime.now();
        this.carrierId = command.carrierId();
        this.managerId = command.managerId();
        this.vehicleId = command.vehicleId();
        this.shipmentId = command.shipmentId();
    }

    public Report updateContent(String newContent) {
        this.content = newContent;
        return this;
    }

    public Report updateTitle(String newTitle) {
        this.title = newTitle;
        return this;
    }

    public Report updateType(ReportType newType) {
        this.type = newType;
        return this;
    }
}
