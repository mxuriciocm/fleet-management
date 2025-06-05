package com.example.fleetmanagement.shipments.domain.model.commands;

import java.time.LocalDateTime;

public record CreateShipmentCommand(String destination, String description, LocalDateTime scheduleDate, Long managerId) {}