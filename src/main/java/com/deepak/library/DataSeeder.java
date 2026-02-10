package com.deepak.library;

import com.deepak.library.domain.Role;
import com.deepak.library.domain.User;
import com.deepak.library.repository.UserRepository;
import com.deepak.library.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LibraryService libraryService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, LibraryService libraryService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.libraryService = libraryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clean up previous data
        userRepository.deleteAll();

        // Create Roles
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        // Create Admin User
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("adminpass"));
            admin.setRoles(Set.of(adminRole, userRole)); // Admin has both roles
            userRepository.save(admin);
            System.out.println("Created ADMIN user.");
        }

        // Create Regular User
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User("user", passwordEncoder.encode("userpass"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Created USER user.");
        }

        // Add some initial books
        libraryService.addBook("The Lord of the Rings", "J.R.R. Tolkien");
        libraryService.addBook("Neuromancer", "William Gibson");
        System.out.println("Seeded initial books.");
    }
}