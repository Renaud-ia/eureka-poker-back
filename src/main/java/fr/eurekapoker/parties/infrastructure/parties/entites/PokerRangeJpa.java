package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="range")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PokerRangeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String idGenere;

    @ManyToMany(mappedBy = "ranges")
    private List<ActionJpa> actions = new ArrayList<>();

    @OneToMany(mappedBy = "range", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<ComboJpa> combos = new ArrayList<>();

    public void ajouterCombo(ComboJpa comboJpa) {
        this.combos.add(comboJpa);
    }

    public void ajouterAction(ActionJpa actionJpa) {
        this.actions.add(actionJpa);
    }
}
