package com.example.fleetmanagement.profile.interfaces.acl.dto;

/**
 * Data Transfer Object for User Profile
 */
public record ProfileDto(
    Long userId,
    String fullName,
    String phoneNumber
) {}
