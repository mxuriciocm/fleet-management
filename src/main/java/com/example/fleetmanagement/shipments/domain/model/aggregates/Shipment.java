package com.example.fleetmanagement.shipments.domain.model.aggregates;

import com.example.fleetmanagement.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.example.fleetmanagement.shipments.domain.model.commands.CreateShipmentCommand;
import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;
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
public class Shipment extends AuditableAbstractAggregateRoot<Shipment> {

    @NotBlank
    @Size(max = 100)
    private String destination;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private LocalDateTime scheduleDate;

    private LocalDateTime startDate;

    private LocalDateTime completedDate;

    @NotNull
    private Long managerId;

    private Long carrierId;


    /**
     * Constructor with required fields.
     * @param destination destination of the shipment
     * @param description description of the shipment
     * @param scheduleDate date when the shipment is scheduled
     * @param managerId id of the manager who owns this shipment
     */
    public Shipment(String destination, String description, LocalDateTime scheduleDate, Long managerId){
        this.destination = destination;
        this.description = description;
        this.status = ShipmentStatus.PENDING;
        this.scheduleDate = scheduleDate;
        this.managerId = managerId;
    }

    public Shipment() {}

    public Shipment(CreateShipmentCommand command) {
        this.destination = command.destination();
        this.description = command.description();
        this.scheduleDate = command.scheduleDate();
        this.managerId = command.managerId();
        this.status = ShipmentStatus.PENDING;
    }

    public Shipment assignCarrier(Long carrierId) {
        this.carrierId = carrierId;
        return this;
    }

    public Shipment removeCarrier() {
        this.carrierId = null;
        return this;
    }

    public Shipment startShipment() {
        if (this.status == ShipmentStatus.PENDING) { throw new IllegalStateException("Cannot start shipment that is not in PENDING status"); }
        if (this.carrierId == null) { throw new IllegalStateException("Cannot start shipment without an assigned carrier"); }
        this.status = ShipmentStatus.IN_PROGRESS;
        this.startDate = LocalDateTime.now();
        return this;
    }

    public Shipment completeShipment() {
        if (this.status != ShipmentStatus.IN_PROGRESS) { throw new IllegalStateException("Cannot complete shipment that is not in IN_PROGRESS status"); }
        this.status = ShipmentStatus.COMPLETED;
        this.completedDate = LocalDateTime.now();
        return this;
    }

    public Shipment cancelShipment() {
        if (this.status == ShipmentStatus.COMPLETED) { throw new IllegalStateException("Cannot cancel a shipment that is already completed"); }
        this.status = ShipmentStatus.CANCELLED;
        return this;
    }

    public Shipment changeStatus(ShipmentStatus status) {
        if (this.status == ShipmentStatus.COMPLETED && status != ShipmentStatus.COMPLETED) { throw new IllegalStateException("Cannot change status of a completed shipment"); }
        if (this.status == ShipmentStatus.PENDING && status == ShipmentStatus.PENDING) { throw new IllegalStateException("Cannot revert shipment status to PENDING"); }
        if (this.status == ShipmentStatus.IN_PROGRESS && this.startDate == null) { this.startDate = LocalDateTime.now(); }
        if (this.status == ShipmentStatus.COMPLETED && this.completedDate == null) { this.completedDate = LocalDateTime.now(); }
        this.status = status;
        return this;
    }
}

