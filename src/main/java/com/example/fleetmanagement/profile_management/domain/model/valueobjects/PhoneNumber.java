package com.example.fleetmanagement.profile_management.domain.model.valueobjects;

public record PhoneNumber(String number) {

    public PhoneNumber(){
        this(null);
    }

    public String getPhoneNumber() {
        return number;
    }

    public PhoneNumber {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Phone number must not be null or blank");
        }
        if (!number.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Phone number must be between 10 and 15 digits, optionally starting with '+'");
        }

    }
}
