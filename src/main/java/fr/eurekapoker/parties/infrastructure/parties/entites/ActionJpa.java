package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="action")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ActionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, unique = true)
    private String idGenere;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "infos_joueur_jpa_id")
    private InfosJoueurJpa infosJoueurJpa;
    @ManyToOne(fetch = FetchType.LAZY)
    private MainJpa main;
    @Column(nullable = false)
    private String nomTour;
    @Column(nullable = false)
    private String boardString;
    @Column(nullable = false)
    private Long boardLong;

    @Column(nullable = true)
    private Integer numeroAction;
    @Column(nullable = true)
    private String nomAction;
    @Column(nullable = true)
    private BigDecimal montantAction;
    @Column(nullable = true)
    private long identifiantSituation;
    @Column(nullable = true)
    private BigDecimal valueAction;
    @Column(nullable = true)
    private BigDecimal pot;
    @Column(nullable = true)
    private BigDecimal potBounty;
    @Column(nullable = true)
    private BigDecimal stackEffectif;
    @Column(nullable = true)
    private Boolean allIn;
    @Setter
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UtilisateurJpa utilisateur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ranges_par_actions",
            joinColumns = @JoinColumn(name = "action_id"),
            inverseJoinColumns = @JoinColumn(name = "range_id"))
    private final List<PokerRangeJpa> ranges = new ArrayList<>();

    public void ajouterRange(PokerRangeJpa pokerRangeJpa) {
        this.ranges.add(pokerRangeJpa);
    }
}
