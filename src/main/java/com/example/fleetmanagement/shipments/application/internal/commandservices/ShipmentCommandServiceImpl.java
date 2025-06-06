package com.example.fleetmanagement.shipments.application.internal.commandservices;

import com.example.fleetmanagement.shipments.domain.model.aggregates.Shipment;
import com.example.fleetmanagement.shipments.domain.model.commands.CreateShipmentCommand;
import com.example.fleetmanagement.shipments.domain.model.commands.UpdateShipmentCommand;
import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;
import com.example.fleetmanagement.shipments.domain.services.ShipmentCommandService;
import com.example.fleetmanagement.shipments.infrastructure.persitence.jpa.repositories.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipmentCommandServiceImpl implements ShipmentCommandService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentCommandServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Handles the creation of a new shipment.
     * @param command The {@link CreateShipmentCommand} Command
     * @return The created {@link Shipment} Aggregate
     */
    @Override
    public Shipment handle(CreateShipmentCommand command) {
        var shipment = new Shipment(command);
        return shipmentRepository.save(shipment);
    }

    /**
     * Handles the update of an existing shipment.
     * @param shipmentId The ID of the shipment to update
     * @param command The {@link UpdateShipmentCommand} Command
     * @return An {@link Optional} containing the updated {@link Shipment} Aggregate, or empty if not found
     */
    @Override
    public Optional<Shipment> handle(Long shipmentId, UpdateShipmentCommand command) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    if (command.destination() != null) {shipment.setDestination(command.destination());}
                    if (command.description() != null) {shipment.setDescription(command.description());}
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Assigns a carrier to a shipment.
     * @param shipmentId the shipment id
     * @param carrierId the carrier id
     * @return An {@link Optional} containing the updated {@link Shipment} Aggregate, or empty if not found
     */
    @Override
    public Optional<Shipment> assignCarrier(Long shipmentId, Long carrierId) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.assignCarrier(carrierId);
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Marks a shipment as delivered.
     * @param shipmentId the shipment id
     * @return An {@link Optional} containing the updated {@link Shipment} Aggregate, or empty if not found
     */
    @Override
    public Optional<Shipment> removeCarrier(Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.removeCarrier();
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Updates the status of a shipment.
     * @param shipmentId the shipment id
     * @param status the new status
     * @return An {@link Optional} containing the updated {@link Shipment} Aggregate, or empty if not found
     */
    @Override
    public Optional<Shipment> updateStatus(Long shipmentId, ShipmentStatus status) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.changeStatus(status);
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Deletes a shipment by its ID.
     * @param shipmentId the shipment id
     */
    @Override
    public Optional<Shipment> startShipment(Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.startShipment();
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Completes a shipment by its ID.
     * @param shipmentId the shipment id
     */
    @Override
    public Optional<Shipment> completeShipment(Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.completeShipment();
                    return shipmentRepository.save(shipment);
                });
    }

    /**
     * Cancels a shipment by its ID.
     * @param shipmentId the shipment id
     */
    @Override
    public Optional<Shipment> cancelShipment(Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .map(shipment -> {
                    shipment.cancelShipment();
                    return shipmentRepository.save(shipment);
                });
    }

    @Override
    public boolean deleteShipment(Long shipmentId) {
        if (shipmentRepository.existsById(shipmentId)) {
            shipmentRepository.deleteById(shipmentId);
            return true;
        }
        return false;
    }
}
