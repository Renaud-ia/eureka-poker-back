package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

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
    private String mailUtilisateur;
    @Column(nullable = false)
    private boolean mailVerifie;
    @Column(nullable = false)
    private String statutMembre;
    @Column(nullable = false)
    private String nomFamille;
    @Column(nullable = false)
    private String prenom;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartieJpa> parties;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoueurJpa> joueurs;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoueurJpa> actions;

    public void ajouterJoueur(JoueurJpa joueurJpa) {
        this.joueurs.add(joueurJpa);
    }
}

