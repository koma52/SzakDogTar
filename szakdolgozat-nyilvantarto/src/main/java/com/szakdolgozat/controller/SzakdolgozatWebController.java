package com.szakdolgozat.controller;

import com.szakdolgozat.service.SzakdolgozatService;
import com.szakdolgozat.szakdolgozat_nyilvantarto.Szakdolgozat;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SzakdolgozatWebController {

    @Autowired
    private SzakdolgozatService service;

    @GetMapping("/szakdolgozatok")
    public String list(Model model) {
        model.addAttribute("szakdolgozatok", service.getAllSzakdolgozatok());
        return "szakdolgozat_list";
    }

    @GetMapping("/szakdolgozat/new")
    public String createForm(Model model) {
        model.addAttribute("szakdolgozat", new Szakdolgozat());
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
    public String update(@PathVariable Long id, @Valid Szakdolgozat szakdolgozat, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("szakdolgozat", szakdolgozat);
            return "szakdolgozat_edit";
        }
        service.updateSzakdolgozat(id, szakdolgozat);
        return "redirect:/szakdolgozatok";
    }

    @GetMapping("/szakdolgozat/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteSzakdolgozat(id);
        return "redirect:/szakdolgozatok";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/szakdolgozatok";
    }


}
