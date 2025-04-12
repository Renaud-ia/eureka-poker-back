package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="utilisateurs")
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
}

