package com.szakdolgozat.service;

import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import com.szakdolgozat.repository.SzakdolgozatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

@Service
public class SzakdolgozatService {

    private final SzakdolgozatRepository repository;

    @Autowired
    public SzakdolgozatService(SzakdolgozatRepository repository) {
        this.repository = repository;
    }


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
                    // Csak akkor frissítjük a filePath-et, ha az új érték nem null vagy üres
                    if (updatedSzakdolgozat.getFilePath() != null && !updatedSzakdolgozat.getFilePath().trim().isEmpty()) {
                        existing.setFilePath(updatedSzakdolgozat.getFilePath());
                    }
                    // Új metaadatok frissítése
                    existing.setStatus(updatedSzakdolgozat.getStatus());
                    existing.setTemaVezeto(updatedSzakdolgozat.getTemaVezeto());
                    existing.setDokumentumTipus(updatedSzakdolgozat.getDokumentumTipus());
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

    //Kereső
    public List<Szakdolgozat> searchSzakdolgozat(Long id, String cim, String szerzo, String status, String temaVezeto) {

        Szakdolgozat probe = new Szakdolgozat();
        if(id != null) {
            probe.setId(id);
        }
        if(cim != null && !cim.isEmpty()){
            probe.setCim(cim);
        }
        if(szerzo != null && !szerzo.isEmpty()){
            probe.setSzerzo(szerzo);
        }
        if(status != null && !status.isEmpty()){
            probe.setStatus(status);
        }
        if(temaVezeto != null && !temaVezeto.isEmpty()){
            probe.setTemaVezeto(temaVezeto);
        }

        // Példakészítő a rugalmas kereséshez: a szöveges mezőket "contains" szemléletben akarjuk keresni
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withMatcher("cim", match -> match.contains().ignoreCase())
                .withMatcher("szerzo", match -> match.contains().ignoreCase())
                .withMatcher("status", match -> match.contains().ignoreCase())
                .withMatcher("temaVezeto", match -> match.contains().ignoreCase());

        Example<Szakdolgozat> example = Example.of(probe, matcher);
        return repository.findAll(example);
    }

    public List<Szakdolgozat> findBySzerzo(String szerzo) {
        return repository.findBySzerzo(szerzo);
    }

    public List<Szakdolgozat> findByTemaVezeto(String temaVezeto) {
        return repository.findByTemaVezeto(temaVezeto);
    }

}
