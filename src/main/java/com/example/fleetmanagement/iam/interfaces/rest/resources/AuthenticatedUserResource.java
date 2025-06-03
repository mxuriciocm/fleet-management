package com.example.fleetmanagement.iam.interfaces.rest.resources;

/**
 * Authenticated user resource.
 */
public record AuthenticatedUserResource(Long id, String email, String token) {
}
