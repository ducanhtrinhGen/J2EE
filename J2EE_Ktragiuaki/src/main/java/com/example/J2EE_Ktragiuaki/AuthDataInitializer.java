package com.example.J2EE_Ktragiuaki;

import com.example.J2EE_Ktragiuaki.entity.Role;
import com.example.J2EE_Ktragiuaki.entity.Student;
import com.example.J2EE_Ktragiuaki.repository.RoleRepository;
import com.example.J2EE_Ktragiuaki.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthDataInitializer {

    @Bean
    CommandLineRunner initAuthData(
        RoleRepository roleRepository,
        StudentRepository studentRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            roleRepository.findByName("STUDENT")
                .orElseGet(() -> roleRepository.save(new Role("STUDENT")));

            Student admin = studentRepository.findByUsername("admin")
                .orElseGet(() -> {
                    Student newAdmin = new Student();
                    newAdmin.setUsername("admin");
                    newAdmin.setPassword(passwordEncoder.encode("admin123"));
                    newAdmin.setEmail("admin@local.test");
                    return newAdmin;
                });

            boolean hasAdminRole = admin.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));

            if (!hasAdminRole) {
                admin.getRoles().add(adminRole);
            }

            if (admin.getId() == null || !hasAdminRole) {
                studentRepository.save(admin);
            }
        };
    }
}
