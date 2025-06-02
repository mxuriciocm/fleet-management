package com.example.fleetmanagement.iam.domain.model.queries;

import com.example.fleetmanagement.iam.domain.model.valueobjects.Roles;

/**
 * Query to get role by name.
 */
public record GetRoleByNameQuery(Roles roleName) {
}
