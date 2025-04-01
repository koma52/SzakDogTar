package com.szakdolgozat.szakdolgozat_nyilvantarto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
public class Szakdolgozat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate beadasDatuma;

    @NotBlank(message = "A cím nem lehet üres")
    private String cim;

    @NotBlank(message = "A szerző megadása kötelező")
    private String szerzo;

    // Getterek, setterek és konstruktorok
    public Szakdolgozat() {
    }

    public Szakdolgozat(String cim, String szerzo) {
        this.cim = cim;
        this.szerzo = szerzo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getSzerzo() {
        return szerzo;
    }

    public void setSzerzo(String szerzo) {
        this.szerzo = szerzo;
    }

    public LocalDate getBeadasDatuma() {
        return beadasDatuma;
    }

    public void setBeadasDatuma(LocalDate beadasDatuma) {
        this.beadasDatuma = beadasDatuma;
    }

}
