package com.example.fleetmanagement.iam.domain.services;

import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import com.example.fleetmanagement.iam.domain.model.queries.GetAllUsersQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByIdQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByUsernameQuery;

import java.util.List;
import java.util.Optional;

/**
 * User query service.
 * <p>
 *     This service is responsible for handling user queries.
 *     It provides methods to handle queries for getting all users, getting a user by id, and getting a user by username.
 * </p>
 */
public interface UserQueryService {
    /**
     * Handle get all users query.
     *
     * @param query the query
     * @return a list of users
     */
    List<User> handle(GetAllUsersQuery query);

    /**
     * Handle get user by id query.
     *
     * @param query the query
     * @return an optional of user if the user was found
     */
    Optional<User> handle(GetUserByIdQuery query);

    /**
     * Handle get user by username query.
     *
     * @param query the query
     * @return an optional of user if the user was found
     */
    Optional<User> handle(GetUserByUsernameQuery query);
}
