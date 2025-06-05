package com.example.fleetmanagement.vehicles.interfaces.rest.resources;

import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;

public record VehicleResource(Long id, String licensePlate, String brand, String model, VehicleStatus status, Long carrierId, Long managerId) {}
