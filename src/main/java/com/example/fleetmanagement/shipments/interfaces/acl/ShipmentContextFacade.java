package com.example.fleetmanagement.shipments.interfaces.acl;

import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;
import com.example.fleetmanagement.shipments.interfaces.acl.dto.ShipmentDto;

import java.util.List;
import java.util.Optional;

public interface ShipmentContextFacade {

    /**
     * Fetches a shipment by its ID.
     * @param shipmentId the ID of the shipment to fetch
     * @return an Optional containing the ShipmentDto if found, or empty if not found
     */
    Optional<ShipmentDto> fetchShipmentById(Long shipmentId);

    /**
     * Fetches all shipments associated with a specific manager.
     * @param managerId the ID of the manager
     * @return a list of ShipmentDto objects associated with the manager
     */
    List<ShipmentDto> fetchShipmentsByManagerId(Long managerId);


    /**
     * Fetches all shipments associated with a specific carrier.
     * @param carrierId the ID of the carrier
     * @return a list of ShipmentDto objects associated with the carrier
     */
    List<ShipmentDto> fetchShipmentsByCarrierId(Long carrierId);


    /**
     * Fetches all shipments with a specific status.
     * @param status the status of the shipments to fetch
     * @return a list of ShipmentDto objects with the specified status
     */
    List<ShipmentDto> fetchShipmentsByStatus(ShipmentStatus status);


    /**
     * Counts the number of active shipments associated with a specific manager.
     * @param managerId the ID of the manager
     * @return the count of active shipments for the specified manager
     */
    int countActiveShipmentsByManagerId(Long managerId);
}
