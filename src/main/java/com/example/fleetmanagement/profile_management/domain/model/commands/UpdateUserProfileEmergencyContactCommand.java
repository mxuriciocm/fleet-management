package com.example.fleetmanagement.profile_management.domain.model.commands;

public record UpdateUserProfileEmergencyContactCommand(Long userId, String newEmergencyContact) {}
