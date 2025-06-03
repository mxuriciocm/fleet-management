package com.example.fleetmanagement.profile_management.domain.model.commands;

import com.example.fleetmanagement.profile_management.domain.model.valueobjects.PhoneNumber;

public record UpdateUserProfilePhoneCommand(Long userId, PhoneNumber newPhone) {}
