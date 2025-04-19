package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="utilisateur")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UtilisateurJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String idGenere;
    @Column(nullable = false)
    private LocalDateTime dateCreation;
    @Setter
    @Column(nullable = false)
    private String statutMembre;

    @Setter
    private String mailUtilisateur;
    @Setter
    private boolean mailVerifie;
    @Setter
    private String nomFamille;
    @Setter
    private String prenom;

    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<PartieJpa> parties = new ArrayList<>();;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<JoueurJpa> joueurs = new ArrayList<>();;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<ActionJpa> actions = new ArrayList<>();;


    public void ajouterJoueur(JoueurJpa joueurJpa) {
        this.joueurs.add(joueurJpa);
    }

    public void ajouterPartie(PartieJpa partieJpa) {
        this.parties.add(partieJpa);
    }

    public void ajouterAction(ActionJpa actionJpa) {
        this.actions.add(actionJpa);
    }
}

