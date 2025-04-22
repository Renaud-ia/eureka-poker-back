package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="combo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ComboJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "range_id", nullable = false)
    private PokerRangeJpa range;
    private String nomCombo;
    private Float frequence;
}
