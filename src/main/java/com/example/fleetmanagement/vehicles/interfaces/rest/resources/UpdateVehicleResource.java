package com.example.fleetmanagement.vehicles.interfaces.rest.resources;

import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;

public record UpdateVehicleResource(String brand, String model, VehicleStatus status) {}
