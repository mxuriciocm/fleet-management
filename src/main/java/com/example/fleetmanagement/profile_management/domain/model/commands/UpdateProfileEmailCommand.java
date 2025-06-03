package com.example.fleetmanagement.profile_management.domain.model.commands;

public record UpdateProfileEmailCommand(Long userId, String newEmail) {}
