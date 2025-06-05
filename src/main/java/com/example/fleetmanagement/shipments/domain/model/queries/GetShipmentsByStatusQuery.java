package com.example.fleetmanagement.shipments.domain.model.queries;

import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;

public record GetShipmentsByStatusQuery(ShipmentStatus status) {}
