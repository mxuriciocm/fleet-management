package com.example.fleetmanagement.profile_management.domain.model.commands;

import com.example.fleetmanagement.iam.domain.model.valueobjects.Roles;
import com.example.fleetmanagement.profile_management.domain.model.valueobjects.PhoneNumber;

public record CreateUserProfileCommand(Long userId, String fullName, String email, PhoneNumber phone, Roles role, String emergencyContact) {}
