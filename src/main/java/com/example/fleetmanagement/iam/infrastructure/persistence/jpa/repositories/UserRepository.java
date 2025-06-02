package com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories;

import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository.
 * <p>
 *     This interface is used to interact with the database to perform CRUD operations on the User entity.
 *     It additionally provides a method to find a user by its username and to check if a user with a given username exists.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by its username.
     *
     * @param username the username of the user to find.
     * @return an optional containing the user if it exists, an empty optional otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a user with a given username exists.
     *
     * @param username the username of the user to check.
     * @return true if a user with the given username exists, false otherwise.
     */
    boolean existsByUsername(String username);
}
