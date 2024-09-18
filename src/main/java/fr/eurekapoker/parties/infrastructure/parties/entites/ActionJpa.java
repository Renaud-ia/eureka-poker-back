package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ActionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
