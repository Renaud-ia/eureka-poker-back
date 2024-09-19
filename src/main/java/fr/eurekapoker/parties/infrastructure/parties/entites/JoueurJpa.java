package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="parties_joueur")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JoueurJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nomRoom;
    @Column(nullable = false)
    private String nomJoueur;
    private boolean joueurAnonyme;
}
