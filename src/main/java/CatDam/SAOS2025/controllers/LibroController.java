package CatDam.SAOS2025.controllers;

import CatDam.SAOS2025.entities.Libro;
import CatDam.SAOS2025.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
//  definisce il path di base per accedere ai vari metodi
@RestController
@RequestMapping("/api/libro")
public class LibroController {
    @Autowired
    private LibroRepository libroRepository;
    @GetMapping("/findAllBooks")
    @PreAuthorize("hasRole('UTENTE') or hasRole('MODERATORE') or hasRole('ADMIN')")
    public List<Libro> findAllBooks() {
        return libroRepository.findAll();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('UTENTE') or hasRole('MODERATORE') or hasRole('ADMIN')")
    public ResponseEntity<Libro> findBookById(@PathVariable(value = "id") String id) {
        Optional<Libro> libro = libroRepository.findById(id);
        if(libro.isPresent()) {
            return ResponseEntity.ok().body(libro.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/saveBook")
    @PreAuthorize("hasRole('MODERATORE') or hasRole('ADMIN')")
    public Libro saveBook(@Validated @RequestBody Libro libro) {
        return libroRepository.save(libro);
    }
}