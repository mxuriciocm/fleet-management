package com.example.fleetmanagement.iam.application.internal.queryservices;

import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import com.example.fleetmanagement.iam.domain.model.queries.GetAllUsersQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByIdQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByUsernameQuery;
import com.example.fleetmanagement.iam.domain.services.UserQueryService;
import com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserQueryService} interface.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository {@link UserRepository} instance.
     */
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // inherited javadoc
    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    // inherited javadoc
    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    // inherited javadoc
    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }
}
