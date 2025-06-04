package com.example.fleetmanagement.profile.interfaces.acl;

import com.example.fleetmanagement.profile.interfaces.acl.dto.ProfileDto;
import java.util.Optional;

/**
 * UserProfileContextFacade
 */
public interface ProfileContextFacade {
    Long createUserProfile(Long userId, String firstName, String lastName, String phoneNumber);

    /**
     * Obtiene los datos de un perfil de usuario por su userId
     * @param userId ID del usuario
     * @return Objeto DTO con los datos del perfil o empty si no existe
     */
    Optional<ProfileDto> fetchProfileByUserId(Long userId);
}
