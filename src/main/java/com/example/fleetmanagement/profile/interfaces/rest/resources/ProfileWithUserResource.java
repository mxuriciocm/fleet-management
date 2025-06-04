package com.example.fleetmanagement.profile.interfaces.rest.resources;

import java.util.List;

/**
 * Recurse for representing a user profile with associated user details.
 * @param id
 * @param fullName
 * @param phoneNumber
 * @param email
 * @param roles
 */
public record ProfileWithUserResource(
    Long id,
    String fullName,
    String phoneNumber,
    // User data
    String email,
    List<String> roles
) {}
