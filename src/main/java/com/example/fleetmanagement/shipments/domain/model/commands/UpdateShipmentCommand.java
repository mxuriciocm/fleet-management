package com.example.fleetmanagement.shipments.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateShipmentCommand(String destination, String description, LocalDateTime scheduledDate) {}
