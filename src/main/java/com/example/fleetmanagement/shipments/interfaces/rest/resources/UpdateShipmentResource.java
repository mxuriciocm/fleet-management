package com.example.fleetmanagement.shipments.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateShipmentResource(String destination, String description, LocalDateTime scheduledDate) {}
