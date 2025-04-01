package com.szakdolgozat.controller;
import com.szakdolgozat.repository.SzakdolgozatRepository;
import com.szakdolgozat.service.SzakdolgozatService;
import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/szakdolgozatok")
public class SzakdolgozatController {

    @Autowired
    private SzakdolgozatRepository repository;

    @Autowired
    private SzakdolgozatService service; // injektáljuk a service-t

    @GetMapping
    public List<Szakdolgozat> getAll() {
        return service.getAllSzakdolgozatok();
    }

    @GetMapping("/{id}")
    public Szakdolgozat getById(@PathVariable Long id) {
        return service.getSzakdolgozatById(id)
                .orElseThrow(() -> new RuntimeException("Szakdolgozat nem található az id alapján: " + id));
    }

    @PostMapping
    public Szakdolgozat create(@RequestBody Szakdolgozat szakdolgozat) {
        return service.createSzakdolgozat(szakdolgozat);
    }

    @PutMapping("/{id}")
    public Szakdolgozat update(@PathVariable Long id, @RequestBody Szakdolgozat szakdolgozat) {
        return service.updateSzakdolgozat(id, szakdolgozat);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteSzakdolgozat(id);
    }
}

