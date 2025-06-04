package com.example.fleetmanagement.profile_management.infrastructure.persistence.jpa.repositories;

import com.example.fleetmanagement.profile_management.domain.model.aggregates.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
