package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="parties_partie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PartieJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String identifiantGenere;
    @Column(nullable = false)
    private String nomRoom;
    @Column(nullable = false)
    private Boolean joueursAnonymes;
    @Column(nullable = false)
    private String varianteJeu;
    @Column(nullable = false)
    private String typeTable;
    private String formatSpecialRoom;
    @Column(nullable = false)
    private Long identifiantParse;
    @Column(nullable = false)
    private String nomPartie;
    @Column(nullable = false)
    private String nomHero;
    @Column(nullable = false)
    private Integer nombreSieges;
    @Column(nullable = false)
    private Integer nombreMains;
    @Column(nullable = false)
    private LocalDateTime dateJeu;
    @Column(nullable = false)
    private boolean stackEnEuros;
    @Column(nullable = true)
    private BigDecimal buyIn;
    @Column(nullable = false)
    private LocalDateTime dateSauvegarde;
    @OneToMany(mappedBy = "partieJpa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MainJpa> mainsJpa;
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = true)
    private UtilisateurJpa utilisateur;
    private String idSessionGenere;

    public void ajouterMain(MainJpa nouvelleMain) {
        this.mainsJpa.add(nouvelleMain);
    }
}
