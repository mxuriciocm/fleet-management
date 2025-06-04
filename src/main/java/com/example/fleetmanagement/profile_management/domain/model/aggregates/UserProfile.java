package com.example.fleetmanagement.profile_management.domain.model.aggregates;

import com.example.fleetmanagement.profile_management.domain.model.commands.CreateUserProfileCommand;
import com.example.fleetmanagement.profile_management.domain.model.valueobjects.PersonName;
import com.example.fleetmanagement.profile_management.domain.model.valueobjects.PhoneNumber;
import com.example.fleetmanagement.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

/**
 * UserProfile aggregate root.
 */
@Entity
public class UserProfile extends AuditableAbstractAggregateRoot<UserProfile> {

    @Embedded
    private PersonName name;

    @Embedded
    private PhoneNumber phoneNumber;

    /**
     * Constructor with first name, last name, and phone number
     * @param firstName First name
     * @param lastName Last name
     * @param phoneNumber Phone number
     */
    public UserProfile(String firstName, String lastName, String phoneNumber){
        this.name = new PersonName(firstName, lastName);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public UserProfile() {}

    public UserProfile(CreateUserProfileCommand command) {
        this.name = new PersonName(command.firstName(), command.lastName());
        this.phoneNumber = new PhoneNumber(command.phoneNumber());
    }

    public String getFullName() { return name.getFullName(); }

    public void updateName(String firstName, String lastName) { this.name = new PersonName(firstName, lastName); }

    public void updatePhoneNumber(String phoneNumber) { this.phoneNumber = new PhoneNumber(phoneNumber); }

    public String getPhoneNumber() {
        return phoneNumber.getPhoneNumber();
    }
}

