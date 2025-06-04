package com.example.fleetmanagement.iam.application.internal.commandservices;

import com.example.fleetmanagement.iam.application.internal.outboundservices.hashing.HashingService;
import com.example.fleetmanagement.iam.application.internal.outboundservices.tokens.TokenService;
import com.example.fleetmanagement.iam.domain.model.aggregates.User;
import com.example.fleetmanagement.iam.domain.model.commands.ChangeEmailCommand;
import com.example.fleetmanagement.iam.domain.model.commands.ChangePasswordCommand;
import com.example.fleetmanagement.iam.domain.model.commands.SignInCommand;
import com.example.fleetmanagement.iam.domain.model.commands.SignUpCommand;
import com.example.fleetmanagement.iam.domain.services.UserCommandService;
import com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.example.fleetmanagement.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.example.fleetmanagement.profile.interfaces.acl.ProfileContextFacade;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation.
 * <p>
 *     This class implements the {@link UserCommandService} interface.
 *     It is used to handle the sign-up and sign in commands.
 * </p>
 *
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final ProfileContextFacade userProfileContextFacade;

    /**
     * Constructor.
     *
     * @param userRepository the {@link UserRepository} user repository.
     * @param roleRepository the {@link RoleRepository} role repository.
     * @param hashingService the {@link HashingService} hashing service.
     * @param tokenService the {@link TokenService} token service.
     * @param userProfileContextFacade the {@link ProfileContextFacade} user profile context facade.
     */
    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository, HashingService hashingService, TokenService tokenService, ProfileContextFacade userProfileContextFacade) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.userProfileContextFacade = userProfileContextFacade;
    }

    // inherited javadoc
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.username()))
            throw new RuntimeException("Email already exists");

        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RuntimeException("Role name not found"))).toList();

        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        var savedUser = userRepository.save(user);
        System.out.println("User created with ID: " + savedUser.getId());

        try {
            // Create user profile automatically after user creation with null values
            Long profileId = userProfileContextFacade.createUserProfile(
                savedUser.getId(),
                null,  // firstName will be handled by PersonName value object
                null,  // lastName will be handled by PersonName value object
                null   // phoneNumber will be handled by PhoneNumber value object
            );
            System.out.println("Profile created with ID: " + profileId);
        } catch (Exception e) {
            System.err.println("Error creating profile: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.of(savedUser);
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

    @Override
    public Optional<User> handle(ChangePasswordCommand command) {
        // Find the user by ID
        var userOptional = userRepository.findById(command.userId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + command.userId());
        }

        var user = userOptional.get();

        // Verify the current password
        if (!hashingService.matches(command.currentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Update the password
        user.setPassword(hashingService.encode(command.newPassword()));
        var savedUser = userRepository.save(user);

        System.out.println("Password changed successfully for user ID: " + user.getId());

        return Optional.of(savedUser);
    }

    @Override
    public Optional<User> handle(ChangeEmailCommand command) {
        // Find the user by ID
        var userOptional = userRepository.findById(command.userId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + command.userId());
        }

        var user = userOptional.get();

        // Verify the password
        if (!hashingService.matches(command.password(), user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        // Check if the new email already exists
        if (userRepository.existsByEmail(command.newEmail())) {
            throw new RuntimeException("Email already exists: " + command.newEmail());
        }

        // Update the email
        user.setEmail(command.newEmail());
        var savedUser = userRepository.save(user);

        System.out.println("Email changed successfully for user ID: " + user.getId());

        return Optional.of(savedUser);
    }
}
