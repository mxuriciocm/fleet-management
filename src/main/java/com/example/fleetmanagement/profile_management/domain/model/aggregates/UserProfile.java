package com.example.fleetmanagement.profile_management.domain.model.aggregates;

import com.example.fleetmanagement.iam.domain.model.valueobjects.Roles;
import com.example.fleetmanagement.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.example.fleetmanagement.profile_management.domain.model.valueobjects.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserProfile aggregate root.
 * Represents the profile information of a user in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserProfile extends AuditableAbstractAggregateRoot<UserProfile> {

    @Column(nullable = false, unique = true)
    private Long userId;  // Reference to IAM User ID

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String fullName;

    @Email
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false)
    private PhoneNumber phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;  // Using IAM Roles enum (ROLE_ADMIN, ROLE_MANAGER, or ROLE_CARRIER)

    @Size(max = 100)
    @Column
    private String emergencyContact;

    /**
     * Creates a new UserProfile
     * @param userId ID from IAM user
     * @param fullName Full name of the user
     * @param email Email from IAM user
     * @param phone Phone number
     * @param role Role from IAM
     */
    public UserProfile(Long userId, String fullName, String email, PhoneNumber phone, Roles role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Updates profile information
     * @param fullName new full name
     * @param phone new phone number
     * @param emergencyContact new emergency contact (only for ROLE_CARRIER)
     */
    public void updateProfile(String fullName, PhoneNumber phone, String emergencyContact) {
        this.fullName = fullName;
        this.phone = phone;

        // Only set emergency contact if user is a carrier
        if (this.role == Roles.ROLE_CARRIER) {
            this.emergencyContact = emergencyContact;
        }
    }

    /**
     * Updates email (should only be called when email is updated in IAM)
     * @param newEmail the new email address
     */
    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Updates role (should only be called when a role is updated in IAM)
     * @param newRole the new role (ROLE_ADMIN, ROLE_MANAGER, or ROLE_CARRIER)
     */
    public void updateRole(Roles newRole) {
        this.role = newRole;
        // Clear emergency contact if a user is no longer a carrier
        if (newRole != Roles.ROLE_CARRIER) {
            this.emergencyContact = null;
        }
    }

    /**
     * Checks if the profile belongs to an admin
     * @return true if the user is an admin
     */
    public boolean isAdmin() {
        return this.role == Roles.ROLE_ADMIN;
    }

    /**
     * Checks if the profile belongs to a manager
     * @return true if the user is a manager
     */
    public boolean isManager() {
        return this.role == Roles.ROLE_MANAGER;
    }

    /**
     * Checks if the profile belongs to a carrier
     * @return true if the user is a carrier
     */
    public boolean isCarrier() {
        return this.role == Roles.ROLE_CARRIER;
    }
}

