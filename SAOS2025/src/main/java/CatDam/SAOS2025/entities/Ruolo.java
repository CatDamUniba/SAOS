package CatDam.SAOS2025.entities;

import CatDam.SAOS2025.models.RuoloEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ruoli")
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RuoloEnum name;
    public Ruolo() {
    }
    public Ruolo(RuoloEnum name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public RuoloEnum getName() {
        return name;
    }
    public void setName(RuoloEnum name) {
        this.name = name;
    }
}