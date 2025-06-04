package com.example.fleetmanagement.iam.application.acl;

import com.example.fleetmanagement.iam.domain.model.commands.SignUpCommand;
import com.example.fleetmanagement.iam.domain.model.entities.Role;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByIdQuery;
import com.example.fleetmanagement.iam.domain.model.queries.GetUserByEmailQuery;
import com.example.fleetmanagement.iam.domain.services.UserCommandService;
import com.example.fleetmanagement.iam.domain.services.UserQueryService;
import com.example.fleetmanagement.iam.interfaces.acl.IamContextFacade;
import com.example.fleetmanagement.iam.interfaces.acl.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * IamContextFacadeImpl
 * <p>
 *     This class provides a facade to the IAM context.
 *     It is used to interact with the IAM context.
 *     It provides methods to create a user, fetch a user by email, fetch an email by user id.
 * </p>
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    /**
     * Constructor
     * @param userCommandService The user command service.
     * @param userQueryService The user query service.
     */
    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    // inherited javadoc
    @Override
    public Long createUser(String email, String password) {
        var signUpCommand = new SignUpCommand(email, password, List.of(Role.getDefaultRole()));
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    // inherited javadoc
    @Override
    public Long createUser(String email, String password, List<String> roleNames) {
        var roles = roleNames == null ? new ArrayList<Role>() : roleNames.stream().map(Role::toRoleFromName).toList();
        var signUpCommand = new SignUpCommand(email, password, roles);
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    // inherited javadoc
    @Override
    public Long fetchUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(getUserByEmailQuery);
        if (user.isEmpty()) return 0L;
        return user.get().getId();
    }

    // inherited javadoc
    @Override
    public String fetchEmailByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return "";
        return user.get().getEmail();
    }

    // inherited javadoc
    @Override
    public Optional<UserDto> fetchUserById(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        var userEntity = user.get();
        var roles = userEntity.getRoles().stream()
                .map(role -> role.getStringName())
                .collect(Collectors.toList());

        return Optional.of(new UserDto(
            userEntity.getId(),
            userEntity.getEmail(),
            roles
        ));
    }
}
