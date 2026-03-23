package com.example.J2EE_Ktragiuaki.service;

import java.util.stream.Collectors;

import com.example.J2EE_Ktragiuaki.entity.Student;
import com.example.J2EE_Ktragiuaki.repository.StudentRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay tai khoan: " + username));

        return User.withUsername(student.getUsername())
            .password(student.getPassword())
            .authorities(student.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet()))
            .build();
    }
}
