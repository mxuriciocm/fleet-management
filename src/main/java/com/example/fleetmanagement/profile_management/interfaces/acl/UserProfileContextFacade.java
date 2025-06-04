package com.example.fleetmanagement.profile_management.interfaces.acl;

/**
 * UserProfileContextFacade
 */
public interface UserProfileContextFacade {
    Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber);
}
