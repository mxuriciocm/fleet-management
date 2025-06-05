package com.example.fleetmanagement.vehicles.interfaces.acl.dto;

import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;

public record VehicleDto(Long id, String licensePlate, String brand, String model, VehicleStatus status, Long carrierId, Long managerId) {}
