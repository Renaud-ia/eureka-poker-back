package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="parties_main")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MainJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long identifiantParse;
    @Column(nullable = false)
    private Integer indexMain;
    @Column(nullable = false)
    private BigDecimal montantBB;
    @Column(nullable = false)
    private Integer positionDealer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partie_jpa_id")
    private PartieJpa partieJpa;
    @OneToMany(mappedBy = "mainJpa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfosJoueurJpa> infosJoueurJpa;
    @OneToMany(mappedBy = "main", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ActionJpa> actions = new ArrayList<>();

    public void ajouterInfosJoueur(InfosJoueurJpa infosJoueurJpa) {
        this.infosJoueurJpa.add(infosJoueurJpa);
    }

    public void ajouterAction(ActionJpa tourJpa) {
        this.actions.add(tourJpa);
    }
}
