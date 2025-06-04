package com.example.fleetmanagement.profile_management.interfaces.acl;

import com.example.fleetmanagement.profile_management.interfaces.acl.dto.UserProfileDto;
import java.util.Optional;

/**
 * UserProfileContextFacade
 */
public interface UserProfileContextFacade {
    Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber);

    /**
     * Obtiene los datos de un perfil de usuario por su userId
     * @param userId ID del usuario
     * @return Objeto DTO con los datos del perfil o empty si no existe
     */
    Optional<UserProfileDto> fetchProfileByUserId(Long userId);
}
