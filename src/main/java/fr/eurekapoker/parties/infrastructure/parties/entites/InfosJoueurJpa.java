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
    private int siege;
    private BigDecimal stack;
    private BigDecimal bounty;
    private String comboJoueurString;
    private int comboJoueurInt;
    private BigDecimal antePayee;
    private BigDecimal blindePayee;
}
