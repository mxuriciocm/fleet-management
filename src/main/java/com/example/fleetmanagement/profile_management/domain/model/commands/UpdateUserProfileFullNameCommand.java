package com.example.fleetmanagement.profile_management.domain.model.commands;

public record UpdateUserProfileFullNameCommand(Long userId, String newFullName) {}
