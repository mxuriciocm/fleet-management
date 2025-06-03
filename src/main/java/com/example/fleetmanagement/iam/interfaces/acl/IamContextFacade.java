package com.example.fleetmanagement.iam.interfaces.acl;

import java.util.List;

/**
 * IamContextFacade
 * <p>
 *     This interface provides a facade to the IAM context.
 *     It is used to interact with the IAM context.
 *     It provides methods to create a user, fetch a user by email, fetch an email by user id.
 * </p>
 */
public interface IamContextFacade {
    /**
     * Creates a user with the given email and password.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The user id of the created user.
     */
    Long createUser(String email, String password);
    /**
     * Creates a user with the given email, password and roles.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param roles The roles of the user.
     * @return The user id of the created user.
     */
    Long createUser(String email, String password, List<String> roles);
    /**
     * Fetches the user id of the user with the given email.
     * @param email The email of the user.
     * @return The user id of the user.
     */
    Long fetchUserIdByEmail(String email);
    /**
     * Fetches the email of the user with the given user id.
     * @param userId The user id of the user.
     * @return The email of the user.
     */
    String fetchEmailByUserId(Long userId);
}
