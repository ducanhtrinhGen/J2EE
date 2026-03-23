package com.example.J2EE_Ktragiuaki.repository;

import java.util.Optional;

import com.example.J2EE_Ktragiuaki.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
