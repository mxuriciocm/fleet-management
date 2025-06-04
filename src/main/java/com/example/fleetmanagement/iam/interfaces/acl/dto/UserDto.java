package com.example.fleetmanagement.iam.interfaces.acl.dto;

import java.util.List;

/**
 * Data Transfer Object for User.
 * @param userId
 * @param email
 * @param roles
 */
public record UserDto(
    Long userId,
    String email,
    List<String> roles
) {}
