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

    private String filePath;
    private String status;
    private String temaVezeto; // Témavezető neve
    private String dokumentumTipus; // Például PDF, DOCX
    private LocalDate feltoltesIdeje = LocalDate.now();

    // Getterek, setterek és konstruktorok
    public Szakdolgozat() {
    }

    public Szakdolgozat(String cim, String szerzo) {
        this.cim = cim;
        this.szerzo = szerzo;
    }
    public Szakdolgozat(String cim, String szerzo, String filePath) {
        this.cim = cim;
        this.szerzo = szerzo;
        this.filePath = filePath;
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
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemaVezeto() {
        return temaVezeto;
    }

    public void setTemaVezeto(String temaVezeto) {
        this.temaVezeto = temaVezeto;
    }

    public String getDokumentumTipus() {
        return dokumentumTipus;
    }

    public void setDokumentumTipus(String dokumentumTipus) {
        this.dokumentumTipus = dokumentumTipus;
    }

    public LocalDate getFeltoltesIdeje() {
        return feltoltesIdeje;
    }

    public void setFeltoltesIdeje(LocalDate feltoltesIdeje) {
        this.feltoltesIdeje = feltoltesIdeje;
    }
}
