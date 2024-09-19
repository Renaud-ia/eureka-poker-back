package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false, unique = true)
    private String identifiantGenere;
    private long identifiantParse;
    private int indexMain;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partie_jpa_id")
    private PartieJpa partieJpa;
    @OneToMany(mappedBy = "mainJpa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfosJoueurJpa> infosJoueurJpa;
    @OneToMany(mappedBy = "mainJpa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourJpa> toursJpa;

    public void ajouterInfosJoueur(InfosJoueurJpa infosJoueurJpa) {
        this.infosJoueurJpa.add(infosJoueurJpa);
    }

    public void ajouterTour(TourJpa tourJpa) {
        this.toursJpa.add(tourJpa);
    }
}
