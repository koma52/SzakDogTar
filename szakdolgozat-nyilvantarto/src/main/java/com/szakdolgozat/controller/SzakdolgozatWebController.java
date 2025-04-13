package com.szakdolgozat.controller;

import com.szakdolgozat.service.FileStorageService;
import com.szakdolgozat.service.SzakdolgozatService;
import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class SzakdolgozatWebController {

    @Autowired
    private SzakdolgozatService service;

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/login";
    }

    @GetMapping("/szakdolgozatok")
    public String list(Model model) {
        // Jelenlegi felhasználó lekérése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Kiderítjük a felhasználó szerepkörét
        boolean isTemavezeto = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_TEMAVEZETO"));
        boolean isDiak = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_DIÁK") || role.equals("ROLE_USER"));

        List<Szakdolgozat> list;

        if(isTemavezeto) {
            // Ha témavezető, akkor csak azokat a dolgozatokat jelenítjük meg, ahol a téma vezetője a saját felhasználónév
            list = service.findByTemaVezeto(currentUsername);
        } else if(isDiak) {
            // Ha diák, akkor csak a saját szakdolgozatát jelenítjük meg (a szerző mező megegyezik a bejelentkezett felhasználóval)
            list = service.findBySzerzo(currentUsername);
        } else {
            // Egyéb esetben (például admin) az összes dolgozat megjelenítése
            list = service.getAllSzakdolgozatok();
        }

        model.addAttribute("szakdolgozatok", list);
        return "szakdolgozat_list";
    }

    @GetMapping("/szakdolgozat/new")
    public String createForm(Model model) {
        // Lekérjük a jelenlegi felhasználót
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean isStudent = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_DIÁK") || role.equals("ROLE_USER"));
        boolean isTemavezeto = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_TEMAVEZETO"));

        Szakdolgozat newSzakdolgozat = new Szakdolgozat();
        if (isStudent) {
            newSzakdolgozat.setSzerzo(currentUsername);
        } else if (isTemavezeto) {
            newSzakdolgozat.setTemaVezeto(currentUsername);
        }

        model.addAttribute("szakdolgozat", newSzakdolgozat);
        return "szakdolgozat_create";
    }

    @PostMapping("/szakdolgozat/create")
    public String create(@Valid Szakdolgozat szakdolgozat, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("szakdolgozat", szakdolgozat);
            return "szakdolgozat_create";
        }
        service.createSzakdolgozat(szakdolgozat);
        return "redirect:/szakdolgozatok";
    }

    @GetMapping("/szakdolgozat/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Szakdolgozat szakdolgozat = service.getSzakdolgozatById(id)
                .orElseThrow(() -> new RuntimeException("Szakdolgozat nem található az id alapján: " + id));
        model.addAttribute("szakdolgozat", szakdolgozat);
        return "szakdolgozat_edit";
    }
    @PostMapping("/szakdolgozat/update/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("szakdolgozat") @Valid Szakdolgozat szakdolgozat,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("szakdolgozat", szakdolgozat);
            return "szakdolgozat_edit";
        }

        // Ha új fájlt töltenek fel, azt elmentjük és beállítjuk a filePath-et
        if (file != null && !file.isEmpty()) {
            String filePath = fileStorageService.storeFile(file);
            szakdolgozat.setFilePath(filePath);
        } else {
            // Ha nincs új fájl, tartsuk meg a korábbi filePath-et!
            Szakdolgozat existing = service.getSzakdolgozatById(id)
                    .orElseThrow(() -> new RuntimeException("Szakdolgozat nem található az id alapján: " + id));
            szakdolgozat.setFilePath(existing.getFilePath());
        }

        // A metaadatok (pl. status, temaVezeto, dokumentumTipus) a formban érkeznek és itt frissítésre kerülnek
        service.updateSzakdolgozat(id, szakdolgozat);
        return "redirect:/szakdolgozatok";
    }


    @GetMapping("/szakdolgozat/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteSzakdolgozat(id);
        return "redirect:/szakdolgozatok";
    }


    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/szakdolgozat/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("cim") String cim,
                             @RequestParam("szerzo") String szerzo,
                             @RequestParam("status") String status,
                             @RequestParam("temaVezeto") String temaVezeto,
                             @RequestParam("dokumentumTipus") String dokumentumTipus) {


        String filePath = fileStorageService.storeFile(file);


        Szakdolgozat szakdolgozat = new Szakdolgozat(cim, szerzo);
        szakdolgozat.setFilePath(filePath);
        szakdolgozat.setStatus(status);
        szakdolgozat.setTemaVezeto(temaVezeto);
        szakdolgozat.setDokumentumTipus(dokumentumTipus);

        service.createSzakdolgozat(szakdolgozat);
        return "redirect:/szakdolgozatok";
    }


    @GetMapping("/szakdolgozat/search")
    public String searchSzakdolgozat(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "cim", required = false) String cim,
            @RequestParam(value = "szerzo", required = false) String szerzo,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "temaVezeto", required = false) String temaVezeto,
            Model model) {

        List<Szakdolgozat> results = service.searchSzakdolgozat(id, cim, szerzo, status, temaVezeto);
        model.addAttribute("szakdolgozatok", results);
        return "szakdolgozat_list";
    }



}
