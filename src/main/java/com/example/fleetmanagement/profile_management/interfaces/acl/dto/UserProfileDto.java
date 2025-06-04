package com.example.fleetmanagement.profile_management.interfaces.acl.dto;

/**
 * Data Transfer Object for User Profile
 */
public record UserProfileDto(
    Long userId,
    String fullName,
    String phoneNumber
) {}
