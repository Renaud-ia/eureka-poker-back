package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MainPersistenceDto {
    // todo coder les méthodes
    // todo coder les vérifications
    private final String idUniqueGenere;
    private final long idParse;
    private final int indexMain;
    private final BigDecimal montantBB;
    private final HashMap<JoueurPersistenceDto, Integer> siegesJoueursPresents;
    private final List<TourPersistanceDto> tours;
    private final HashMap<String, String> cartesJoueursString;
    private final HashMap<String, Integer> cartesJoueursInt;
    private final HashMap<String, BigDecimal> blindes;
    private final HashMap<String, BigDecimal> antes;
    private final HashMap<String, BigDecimal> resultats;
    private final HashMap<String, BigDecimal> valueParAction;
    private final HashMap<String, BigDecimal> stackDepart;
    private final HashMap<String, BigDecimal> bounty;
    private String nomHero;
    private int positionDealer;
    public MainPersistenceDto(String idUniqueGenere,
                              long idParse,
                              BigDecimal montantBB,
                              int indexMain) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.indexMain = indexMain;
        this.montantBB = montantBB;
        this.siegesJoueursPresents = new HashMap<>();
        this.tours = new ArrayList<>();
        this.cartesJoueursString = new HashMap<>();
        this.cartesJoueursInt = new HashMap<>();
        this.blindes = new HashMap<>();
        this.antes = new HashMap<>();
        this.resultats = new HashMap<>();
        this.valueParAction = new HashMap<>();
        this.stackDepart = new HashMap<>();
        this.bounty = new HashMap<>();
    }

    public void ajouterJoueur(JoueurPersistenceDto nouveauJoueur, int numeroSiege) {
        this.siegesJoueursPresents.put(nouveauJoueur, numeroSiege);
    }

    public void ajouterBlinde(String nomJoueur, BigDecimal montant) {
        this.blindes.put(nomJoueur, montant);
    }

    public void ajouterAnte(String nomJoueur, BigDecimal montant) {
        this.antes.put(nomJoueur, montant);
    }

    public void ajouterTour(TourPersistanceDto nouveauTour) {
        this.tours.add(nouveauTour);
    }

    public void ajouterGains(String nomJoueur, BigDecimal montant) {
        this.resultats.put(nomJoueur, montant);
    }

    public void ajouterCartes(String nomJoueur, int comboAsInt, String comboAsString) {
        this.cartesJoueursInt.put(nomJoueur, comboAsInt);
        this.cartesJoueursString.put(nomJoueur, comboAsString);
    }

    public void ajouterPositionDealer(int positionDealer) {
        this.positionDealer = positionDealer;
    }

    public void fixNombreActionsDuJoueur(String nomJoueur, int nombreActions) {
        BigDecimal resultat = this.resultats.getOrDefault(nomJoueur, new BigDecimal(0));
        if (nombreActions == 0) resultat = new BigDecimal("0");
        else resultat = resultat.divide(BigDecimal.valueOf(nombreActions), RoundingMode.HALF_UP);
        this.valueParAction.put(nomJoueur, resultat);
    }

    public void ajouterInfosHero(String nomHero, int comboAsInt, String comboAsString) {
        this.nomHero = nomHero;
        this.cartesJoueursInt.put(nomHero, comboAsInt);
        this.cartesJoueursString.put(nomHero, comboAsString);
    }

    public List<String> obtNomsJoueursPresents() {
        List<String> nomJoueursPresents = new ArrayList<>();
        for (JoueurPersistenceDto joueurPersistenceDto : this.siegesJoueursPresents.keySet()) {
            nomJoueursPresents.add(joueurPersistenceDto.obtNomJoueur());
        }

        return nomJoueursPresents;
    }

    public String obtNomHero() {
        return nomHero;
    }

    public int obtNombreJoueurs() {
        return this.siegesJoueursPresents.size();
    }

    public BigDecimal obtValueParActionJoueur(String nomJoueur) {
        return valueParAction.get(nomJoueur);
    }

    public String obtIdentifiantGenere() {
        return idUniqueGenere;
    }

    public long obtIdParse() {
        return idParse;
    }

    public Set<JoueurPersistenceDto> obtJoueursPresents() {
        return siegesJoueursPresents.keySet();
    }

    public int obtSiege(JoueurPersistenceDto joueurPersistenceDto) {
        return siegesJoueursPresents.get(joueurPersistenceDto);
    }

    public void ajouterStackDepart(String nomJoueur, BigDecimal montant) {
        this.stackDepart.put(nomJoueur, montant);
    }

    public void ajouterBounty(String nomJoueur, BigDecimal montant) {
        this.bounty.put(nomJoueur, montant);
    }

    public BigDecimal obtStack(String nomJoueur) {
        return stackDepart.get(nomJoueur);
    }

    public BigDecimal obtBounty(String nomJoueur) {
        return bounty.getOrDefault(nomJoueur, new BigDecimal(0));
    }

    public String obtComboAsString(String nomJoueur) {
        return cartesJoueursString.getOrDefault(nomJoueur, "");
    }

    public int obtComboAsInt(String nomJoueur) {
        return cartesJoueursInt.getOrDefault(nomJoueur, 0);
    }

    public BigDecimal obtAnte(String nomJoueur) {
        return antes.getOrDefault(nomJoueur, new BigDecimal(0));
    }

    public BigDecimal obtBlinde(String nomJoueur) {
        return blindes.getOrDefault(nomJoueur, new BigDecimal(0));
    }

    public BigDecimal obtGains(String nomJoueur) {
        return resultats.getOrDefault(nomJoueur, new BigDecimal(0));
    }

    public List<TourPersistanceDto> obtTours() {
        return tours;
    }

    public int obtPositionDealer() {
        return positionDealer;
    }

    public BigDecimal obtMontantBB() {
        return montantBB;
    }
}
