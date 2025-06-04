package com.example.fleetmanagement.profile_management.interfaces.rest.resources;

public record CreateUserProfileResource(String firstName, String lastName, String phoneNumber) {

    public CreateUserProfileResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
    }
}
