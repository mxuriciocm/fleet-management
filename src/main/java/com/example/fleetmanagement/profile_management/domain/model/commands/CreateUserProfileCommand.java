package com.example.fleetmanagement.profile_management.domain.model.commands;

/**
 * Create User Profile Command
 * @param firstName
 * @param lastName
 * @param phoneNumber
 */
public record CreateUserProfileCommand(String firstName, String lastName, String phoneNumber) {}
