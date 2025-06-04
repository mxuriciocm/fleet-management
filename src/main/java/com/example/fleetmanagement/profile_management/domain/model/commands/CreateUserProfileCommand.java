package com.example.fleetmanagement.profile_management.domain.model.commands;

/**
 * Create User Profile Command
 * @param userId ID of the user this profile belongs to
 * @param firstName First name
 * @param lastName Last name
 * @param phoneNumber Phone number
 */
public record CreateUserProfileCommand(Long userId, String firstName, String lastName, String phoneNumber) {}
