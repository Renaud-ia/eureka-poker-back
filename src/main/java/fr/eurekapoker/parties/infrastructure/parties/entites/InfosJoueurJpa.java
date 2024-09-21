package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "parties_infos_joueur")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class InfosJoueurJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "joueur_jpa_id")
    private JoueurJpa joueurJpa;
    @ManyToOne
    @JoinColumn(name = "main_jpa_id")
    private MainJpa mainJpa;
    @Column(nullable = false)
    private int siege;
    @Column(nullable = false)
    private BigDecimal stack;
    @Column(nullable = false)
    private BigDecimal bounty;
    @Column(nullable = false)
    private String comboJoueurString;
    @Column(nullable = false)
    private int comboJoueurInt;
    @Column(nullable = false)
    private BigDecimal antePayee;
    @Column(nullable = false)
    private BigDecimal blindePayee;
    @Column(nullable = false)
    private BigDecimal gains;
}
