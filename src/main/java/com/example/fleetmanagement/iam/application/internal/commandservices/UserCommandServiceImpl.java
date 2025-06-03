package com.example.fleetmanagement.iam.application.internal.commandservices;

import com.example.fleetmanagement.iam.application.internal.outboundservices.hashing.HashingService;
import com.example.fleetmanagement.iam.application.internal.outboundservices.tokens.TokenService;
import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import com.example.fleetmanagement.iam.domain.model.commands.SignInCommand;
import com.example.fleetmanagement.iam.domain.model.commands.SignUpCommand;
import com.example.fleetmanagement.iam.domain.services.UserCommandService;
import com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation.
 * <p>
 *     This class implements the {@link UserCommandService} interface.
 *     It is used to handle the sign up and sign in commands.
 * </p>
 *
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    /**
     * Constructor.
     *
     * @param userRepository the {@link UserRepository} user repository.
     * @param roleRepository the {@link RoleRepository} role repository.
     * @param hashingService the {@link HashingService} hashing service.
     * @param tokenService the {@link TokenService} token service.
     */
    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository, HashingService hashingService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    // inherited javadoc
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.username()))
            throw new RuntimeException("Email already exists");
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RuntimeException("Role name not found"))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByEmail(command.username());
    }

    // inherited javadoc
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.username())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.getEmail());
        return Optional.of(new ImmutablePair<>(user, token));
    }
}
