package com.example.fleetmanagement.iam.interfaces.rest.resources;

import java.util.List;

/**
 * Recurse who contains user information along with profile details.
 * @param id
 * @param email
 * @param roles
 * @param fullName
 * @param phoneNumber
 */
public record UserWithProfileResource(
    Long id,
    String email,
    List<String> roles,
    // Profile attributes
    String fullName,
    String phoneNumber
) {}
