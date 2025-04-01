package com.szakdolgozat.service;

import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import com.szakdolgozat.repository.SzakdolgozatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SzakdolgozatService {

    private final SzakdolgozatRepository repository;

    @Autowired
    public SzakdolgozatService(SzakdolgozatRepository repository) {
        this.repository = repository;
    }

    // Létrehozás, validációval (például ellenőrizheted, hogy a cím nem üres)
    @Transactional
    public Szakdolgozat createSzakdolgozat(Szakdolgozat szakdolgozat) {
        if(szakdolgozat.getCim() == null || szakdolgozat.getCim().trim().isEmpty()){
            throw new IllegalArgumentException("A szakdolgozat címe nem lehet üres.");
        }
        return repository.save(szakdolgozat);
    }

    // Összes szakdolgozat lekérdezése
    public List<Szakdolgozat> getAllSzakdolgozatok() {
        return repository.findAll();
    }

    // Egy szakdolgozat lekérdezése ID alapján
    public Optional<Szakdolgozat> getSzakdolgozatById(Long id) {
        return repository.findById(id);
    }

    // Frissítés
    @Transactional
    public Szakdolgozat updateSzakdolgozat(Long id, Szakdolgozat updatedSzakdolgozat) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCim(updatedSzakdolgozat.getCim());
                    existing.setSzerzo(updatedSzakdolgozat.getSzerzo());
                    // egyéb mezők frissítése...
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Szakdolgozat nem található az id alapján: " + id));
    }

    // Törlés
    @Transactional
    public void deleteSzakdolgozat(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Szakdolgozat nem található az id alapján: " + id);
        }
        repository.deleteById(id);
    }
}
