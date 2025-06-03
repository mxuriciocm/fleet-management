package com.example.fleetmanagement.profile_management.domain.model.commands;

import com.example.fleetmanagement.iam.domain.model.valueobjects.Roles;

public record UpdateUserProfileRoleCommand(Long userId, Roles newRole) {}
