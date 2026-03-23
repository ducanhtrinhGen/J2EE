package com.example.J2EE_Ktragiuaki.service;

import com.example.J2EE_Ktragiuaki.dto.StudentRegistrationForm;
import com.example.J2EE_Ktragiuaki.entity.Role;
import com.example.J2EE_Ktragiuaki.entity.Student;
import com.example.J2EE_Ktragiuaki.repository.RoleRepository;
import com.example.J2EE_Ktragiuaki.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentRegistrationService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentRegistrationService(
        StudentRepository studentRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean usernameExists(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Transactional
    public void registerStudent(StudentRegistrationForm form) {
        Role studentRole = roleRepository.findByName("STUDENT")
            .orElseGet(() -> roleRepository.save(new Role("STUDENT")));

        Student student = new Student();
        student.setUsername(form.getUsername());
        student.setPassword(passwordEncoder.encode(form.getPassword()));
        student.setEmail(form.getEmail());
        student.getRoles().add(studentRole);

        studentRepository.save(student);
    }
}
