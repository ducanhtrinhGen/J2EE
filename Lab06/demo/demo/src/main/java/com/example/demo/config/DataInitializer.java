package com.example.demo.config;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Initialize test accounts if they don't exist
        if (accountRepository.findByLoginName("admin").isEmpty()) {
            Role adminRole = roleRepository.findAll().stream()
                    .filter(r -> r.getName().equals("ROLE_ADMIN"))
                    .findFirst()
                    .orElse(null);

            Account admin = new Account();
            admin.setLogin_name("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            accountRepository.save(admin);
        }

        if (accountRepository.findByLoginName("user1").isEmpty()) {
            Role userRole = roleRepository.findAll().stream()
                    .filter(r -> r.getName().equals("ROLE_USER"))
                    .findFirst()
                    .orElse(null);

            Account user = new Account();
            user.setLogin_name("user1");
            user.setPassword(passwordEncoder.encode("user123"));
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);
            accountRepository.save(user);
        }
    }
}
