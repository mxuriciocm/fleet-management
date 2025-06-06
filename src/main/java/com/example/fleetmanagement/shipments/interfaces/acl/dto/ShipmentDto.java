package com.example.fleetmanagement.shipments.interfaces.acl.dto;

import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentDto(Long id, String destination, String description, ShipmentStatus status, LocalDateTime scheduledDate, LocalDateTime startedDate, LocalDateTime completedDate, Long managerId, Long carrierId) {}
