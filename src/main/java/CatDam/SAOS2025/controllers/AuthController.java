package CatDam.SAOS2025.controllers;

import CatDam.SAOS2025.entities.Ruolo;
import CatDam.SAOS2025.entities.Utente;
import CatDam.SAOS2025.models.RuoloEnum;
import CatDam.SAOS2025.payload.Credenziali;
import CatDam.SAOS2025.payload.Registrazione;
import CatDam.SAOS2025.repositories.RuoloRepository;
import CatDam.SAOS2025.repositories.UtenteRepository;
import CatDam.SAOS2025.response.JwtResponse;
import CatDam.SAOS2025.response.MessageResponse;
import CatDam.SAOS2025.security.JwtUtils;
import CatDam.SAOS2025.security.services.DettagliUtente;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController{
    @Autowired
    private UtenteRepository userRepo;
    @Autowired private JwtUtils jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RuoloRepository roleRepository;
    
    @GetMapping("/csrf")
    public ResponseEntity<?> getCsrfToken(CsrfToken token) {
        return ResponseEntity.ok().body(Map.of("token", token.getToken()));
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody Credenziali loginRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        DettagliUtente userDetails = (DettagliUtente) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    @PostMapping("/registrazione")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Registrazione signUpRequest) {
        Optional<Utente> username = userRepo.cercaPerUsername(signUpRequest.getUsername());
        if (username.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username già presente!"));
        }
        Optional<Utente> email = userRepo.cercaPerEmail(signUpRequest.getEmail());
        if (email.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email già presente!"));
        }
        Utente user = new Utente(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRuoli();
        Set<Ruolo> roles = new HashSet<>();
        System.out.println (strRoles);
        if (strRoles == null) {
            Ruolo userRole = (Ruolo) roleRepository.cercaPerNome(RuoloEnum.UTENTE)
                    .orElseThrow(() -> new RuntimeException("Error: Role "+RuoloEnum.UTENTE+" is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Ruolo adminRole = (Ruolo) roleRepository.cercaPerNome(RuoloEnum.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role "+RuoloEnum.ADMIN+" is not found"));
                        roles.add(adminRole);
                        break;
                    case "MODERATORE":
                        Ruolo modRole = (Ruolo) roleRepository.cercaPerNome(RuoloEnum.MODERATORE)
                                .orElseThrow(() -> new RuntimeException("Error: Role "+RuoloEnum.MODERATORE+" is not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Ruolo userRole = (Ruolo) roleRepository.cercaPerNome(RuoloEnum.UTENTE)
                                .orElseThrow(() -> new RuntimeException("Error: Role "+RuoloEnum.UTENTE+" is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepo.save(user);
        return ResponseEntity.ok(new MessageResponse("Utente registrato correttamente!"));
    }
}