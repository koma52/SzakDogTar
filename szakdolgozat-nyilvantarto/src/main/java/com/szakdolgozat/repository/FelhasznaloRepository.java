package com.szakdolgozat.repository;

import com.szakdolgozat.user.Felhasznalo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FelhasznaloRepository extends JpaRepository<Felhasznalo, Long> {
    Optional<Felhasznalo> findByUsername(String username);
}