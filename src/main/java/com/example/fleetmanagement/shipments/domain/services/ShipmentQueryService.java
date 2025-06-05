package com.example.fleetmanagement.shipments.domain.services;

import com.example.fleetmanagement.shipments.domain.model.aggregates.Shipment;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentByIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByCarrierIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByManagerIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByStatusQuery;

import java.util.List;
import java.util.Optional;

public interface ShipmentQueryService {

    /**
     * Handle Get Shipment By id Query
     * @param query The {@link GetShipmentByIdQuery} Query
     * @return An {@link Optional<Shipment>} instance if the shipment was found, otherwise empty
     */
    Optional<Shipment> handle(GetShipmentByIdQuery query);

    /**
     * Handle Get Shipments By Status Query
     * @param query The {@link GetShipmentsByStatusQuery} Query
     * @return A list of shipments with the specified status
     */
    List<Shipment> handle(GetShipmentsByStatusQuery query);

    /**
     * Handle Get Shipments By Manager id Query
     * @param query The {@link GetShipmentsByManagerIdQuery} Query
     * @return A list of shipments managed by the specified manager
     */
    List<Shipment> handle(GetShipmentsByManagerIdQuery query);

    /**
     * Handle Get Shipments By Carrier id Query
     * @param query The {@link GetShipmentsByCarrierIdQuery} Query
     * @return A list of shipments assigned to the specified carrier
     */
    List<Shipment> handle(GetShipmentsByCarrierIdQuery query);
}
