package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parties_joueur", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "utilisateur_id", "nom_joueur", "nom_room" })
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JoueurJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String idGenere;
    @Column(nullable = false)
    private String nomRoom;
    @Column(nullable = false)
    private String nomJoueur;
    private boolean joueurAnonyme;
    private String notesJoueur;
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UtilisateurJpa utilisateur;
}
