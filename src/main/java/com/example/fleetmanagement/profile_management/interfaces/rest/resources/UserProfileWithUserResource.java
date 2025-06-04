package com.example.fleetmanagement.profile_management.interfaces.rest.resources;

import java.util.List;

/**
 * Recurse for representing a user profile with associated user details.
 * @param id
 * @param fullName
 * @param phoneNumber
 * @param email
 * @param roles
 */
public record UserProfileWithUserResource(
    Long id,
    String fullName,
    String phoneNumber,
    // User data
    String email,
    List<String> roles
) {}
