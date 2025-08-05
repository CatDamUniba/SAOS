package CatDam.SAOS2025.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import CatDam.SAOS2025.entities.Ruolo;
import CatDam.SAOS2025.models.RuoloEnum;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    @Query(value = "SELECT * FROM ruoli WHERE name LIKE %?1%", nativeQuery = true)
    Optional<Ruolo> cercaPerNome(RuoloEnum name);
    @Query(value = "SELECT * FROM ruoli WHERE name LIKE %?1%", nativeQuery = true)
    List<Ruolo> cercaPerNomeList(RuoloEnum name);
}