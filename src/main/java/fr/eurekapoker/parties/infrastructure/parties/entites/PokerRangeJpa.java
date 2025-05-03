package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    @Column(nullable = false)
    private String typeRange;

    @Lob
    @Convert(converter = RangeMapConverter.class)
    @Column(name = "combos", columnDefinition = "TEXT")
    @Setter
    private Map<String, Float> combos;

    public void ajouterAction(ActionJpa actionJpa) {
        this.actions.add(actionJpa);
    }
}
