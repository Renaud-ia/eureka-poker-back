package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="parties_partie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PartieJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String identifiantGenere;
    private String nomRoom;
    private String varianteJeu;
    private String typeTable;
    private long identifiantParse;
    private String nomPartie;
    private String nomHero;
    private LocalDateTime dateJeu;
    private LocalDateTime dateSauvegarde;
    @OneToMany(mappedBy = "partieJpa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MainJpa> mainsJpa;

    public void ajouterMain(MainJpa nouvelleMain) {
        this.mainsJpa.add(nouvelleMain);
    }
}
