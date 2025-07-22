package CatDam.SAOS2025.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/public")
    public String allAccess() {
        return "HELLO WORLD!";
    }

    @GetMapping("/utente")
    @PreAuthorize("hasAuthority('UTENTE') or hasAuthority('MODERATORE') or hasAuthority('ADMIN')")
    public String userAccess() {return "HELLO WORLD! UTENTE";}
    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('MODERATORE')")
    public String moderatorAccess() {
        return "HELLO WORLD! MODERATORE";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminAccess() {
        return "HELLO WORLD! ADMIN";
    }
}