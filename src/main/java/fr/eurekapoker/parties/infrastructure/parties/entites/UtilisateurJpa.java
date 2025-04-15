package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Column(nullable = false)
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

    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartieJpa> parties;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoueurJpa> joueurs;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionJpa> actions;


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

