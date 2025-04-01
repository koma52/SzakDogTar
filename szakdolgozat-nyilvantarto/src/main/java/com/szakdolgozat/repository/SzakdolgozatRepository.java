package com.szakdolgozat.repository;

import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SzakdolgozatRepository extends JpaRepository<Szakdolgozat, Long> {
    List<Szakdolgozat> findByCimContainingIgnoreCase(String cim);
    List<Szakdolgozat> findBySzerzoContainingIgnoreCase(String szerzo);
}
