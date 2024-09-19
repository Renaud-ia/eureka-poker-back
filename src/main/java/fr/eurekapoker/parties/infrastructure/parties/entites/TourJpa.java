package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="parties_tour")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TourJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private MainJpa mainJpa;
    @Column(nullable = false)
    private String nomTour;
    @Column(nullable = false)
    private String boardString;
    @Column(nullable = false)
    private Long boardLong;
    @OneToMany(mappedBy = "tourJpa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionJpa> actionsJpas;

    public void ajouterAction(ActionJpa actionJpa) {
        this.actionsJpas.add(actionJpa);
    }
}
