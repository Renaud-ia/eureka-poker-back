package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="parties_action")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ActionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "infos_joueur_jpa_id")
    private InfosJoueurJpa infosJoueurJpa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_joueur_jpa_id")
    private TourJpa tourJpa;
    @Column(nullable = false)
    private int numeroAction;
    @Column(nullable = false)
    private String nomAction;
    @Column(nullable = false)
    private BigDecimal montantAction;
    @Column(nullable = false)
    private long identifiantSituation;
    @Column(nullable = false)
    private BigDecimal valueAction;
    @Column(nullable = false)
    private BigDecimal pot;
    @Column(nullable = false)
    private BigDecimal potBounty;
    @Column(nullable = false)
    private BigDecimal stackEffectif;
    @Column(nullable = false)
    private Boolean allIn;
    @Setter
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UtilisateurJpa utilisateur;
}
