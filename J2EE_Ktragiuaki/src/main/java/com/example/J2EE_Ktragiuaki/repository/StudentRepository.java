package com.example.J2EE_Ktragiuaki.repository;

import java.util.Optional;

import com.example.J2EE_Ktragiuaki.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Student> findByUsername(String username);
}
