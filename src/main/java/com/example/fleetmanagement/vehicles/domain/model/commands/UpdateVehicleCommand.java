package com.example.fleetmanagement.vehicles.domain.model.commands;

import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;

/**
 * Update Vehicle Command
 * @param brand The brand/manufacturer of the vehicle
 * @param model The model of the vehicle
 * @param status The operational status of the vehicle
 */
public record UpdateVehicleCommand(String brand, String model, VehicleStatus status) {}
