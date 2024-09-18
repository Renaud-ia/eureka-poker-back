package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;
import java.util.*;

public class MainPersistenceDto {
    // todo coder les méthodes
    // todo coder les vérifications
    private final String idUniqueGenere;
    private final long idParse;
    private final int indexMain;
    private final HashSet<JoueurPersistenceDto> joueursPresents;
    private final List<TourPersistanceDto> tours;
    private final HashMap<JoueurPersistenceDto, String> cartesJoueursString;
    private final HashMap<JoueurPersistenceDto, Integer> cartesJoueursInt;
    private final HashMap<JoueurPersistenceDto, Integer> sieges;
    private String nomHero;
    private String joueurAffiche;
    private int positionDealer;
    public MainPersistenceDto(String idUniqueGenere,
                              long idParse,
                              int indexMain) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.indexMain = indexMain;
        this.joueursPresents = new HashSet<>();
        this.tours = new ArrayList<>();
        this.cartesJoueursString = new HashMap<>();
        this.cartesJoueursInt = new HashMap<>();
        this.sieges = new HashMap<>();
    }

    public void ajouterJoueur(JoueurPersistenceDto nouveauJoueur) {
        this.joueursPresents.add(nouveauJoueur);
    }

    public void ajouterBlinde(String nomJoueur, BigDecimal montant) {
    }

    public void ajouterAnte(String nomJoueur, BigDecimal montant) {
    }

    public void ajouterComboHero(int comboAsString, String string) {
    }

    public int obtNombreJoueurs() {
        return sieges.size();
    }

    public void ajouterTour(TourPersistanceDto nouveauTour) {
        this.tours.add(nouveauTour);
    }

    public void ajouterGains(String nomJoueur, BigDecimal montant) {
    }

    public void ajouterCartes(String nomJoueur, int anInt, String string) {
    }

    public List<String> obtNomsJoueursPresents() {
        List<String> nomJoueursPresents = new ArrayList<>();
        for (JoueurPersistenceDto joueurPersistenceDto : this.joueursPresents) {
            nomJoueursPresents.add(joueurPersistenceDto.obtNomJoueur());
        }

        return nomJoueursPresents;
    }

    public String obtNomHero() {
        return nomHero;
    }
}
