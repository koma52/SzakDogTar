package com.szakdolgozat.user;

import com.szakdolgozat.repository.FelhasznaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private FelhasznaloRepository felhasznaloRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Ellenőrizzük, hogy létezik-e az admin fiók
        if (felhasznaloRepository.findByUsername("admin").isEmpty()) {
            Felhasznalo admin = new Felhasznalo("admin", passwordEncoder.encode("password"), "admin@example.com", "ROLE_ADMIN");
            felhasznaloRepository.save(admin);
            System.out.println("Admin fiók létrejött!");
        }
    }
}
