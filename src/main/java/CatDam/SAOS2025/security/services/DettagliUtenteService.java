package CatDam.SAOS2025.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import CatDam.SAOS2025.entities.Utente;
import CatDam.SAOS2025.repositories.UtenteRepository;
import jakarta.transaction.Transactional;

@Service
public class DettagliUtenteService implements UserDetailsService {
    @Autowired
    UtenteRepository utenteRepository;
    @Transactional
    public DettagliUtente loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente user = (Utente) utenteRepository.cercaPerUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return DettagliUtente.build(user);
    }
}