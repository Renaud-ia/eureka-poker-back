package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime derniereModification;

    @ManyToMany(mappedBy = "ranges")
    private final List<ActionJpa> actions = new ArrayList<>();

    @Column(nullable = false)
    private String methodeGeneration;

    @Lob
    @Convert(converter = RangeMapConverter.class)
    @Column(name = "combos", columnDefinition = "TEXT")
    private Map<String, Float> combos;

    public void ajouterAction(ActionJpa actionJpa) {
        this.actions.add(actionJpa);
    }
}
