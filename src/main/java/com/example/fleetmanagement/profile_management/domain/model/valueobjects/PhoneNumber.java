package com.example.fleetmanagement.profile_management.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Embeddable
public class PhoneNumber {
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^[+]?\\d{7,20}$", message = "Invalid phone number")
    private String value;

    protected PhoneNumber() {}

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Phone number cannot be blank");
        if (!value.matches("^[+]?\\d{7,20}$")) throw new IllegalArgumentException("Invalid phone number");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

