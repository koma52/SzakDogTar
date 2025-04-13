package com.szakdolgozat.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegistrationRequest {

    @NotBlank(message = "Felhasználónév szükséges!")
    private String username;

    @NotBlank(message = "Jelszó szükséges!")
    private String password;

    @NotBlank(message = "Email szükséges!")
    @Email(message = "Érvénytelen email cím!")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(student\\.)?uni-miskolc\\.hu$",
            message = "Az e-mail címnek a következő sablonoknak kell megfelelnie: nev@student.uni-miskolc.hu vagy nev@uni-miskolc.hu")
    private String email;

    // Konstruktorok
    public RegistrationRequest() {
    }

    public RegistrationRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getterek és Setterek
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

