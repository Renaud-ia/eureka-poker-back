package fr.eurekapoker.parties.domaine.poker.mains;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * orchestre la récupération des données sur une main de poker
 * expose une interface à la partie application pour récupérer toutes les données
 */
public class MainPoker {
    private final long identifiantMain;
    private final HashMap<String, JoueurPoker> joueursPresents;
    private final List<TourPoker> toursJoues;
    private final HashMap<JoueurPoker, BigDecimal> blindesPayees;
    private final HashMap<JoueurPoker, BigDecimal> antesPayees;
    private List<CartePoker> cartesHero;
    public MainPoker(long identifiantMain) {
        this.identifiantMain = identifiantMain;
        this.joueursPresents = new HashMap<>();
        this.toursJoues = new ArrayList<>();
        this.blindesPayees = new HashMap<>();
        this.antesPayees = new HashMap<>();
    }
    public void ajouterJoueur(String nomJoueur) throws ErreurLectureFichier {
        if (joueursPresents.containsKey(nomJoueur))
            throw new ErreurLectureFichier("Le joueur a déjà été ajouté: " + nomJoueur);
        JoueurPoker nouveauJoueur = new JoueurPoker(nomJoueur);
        this.joueursPresents.put(nomJoueur, nouveauJoueur);
    }

    public List<JoueurPoker> obtJoueurs() {
        return joueursPresents.values().stream().toList();
    }

    public void ajouterTour(TourPoker tourPoker) {
        this.toursJoues.add(tourPoker);
    }

    public List<TourPoker> obtTours() {
        return toursJoues;
    }

    public void ajouterBlinde(String nomJoueur, BigDecimal montant) throws ErreurLectureFichier {
        JoueurPoker joueurPoker = obtJoueurParNom(nomJoueur);
        if (blindesPayees.containsKey(joueurPoker))
            throw new ErreurLectureFichier("Blindes ajoutées deux fois pour:" + joueurPoker);
        blindesPayees.put(joueurPoker, montant);
    }

    public void ajouterAnte(String nomJoueur, BigDecimal montant) throws ErreurLectureFichier {
        JoueurPoker joueurPoker = obtJoueurParNom(nomJoueur);
        if (antesPayees.containsKey(joueurPoker))
            throw new ErreurLectureFichier("Blindes ajoutées deux fois pour:" + joueurPoker);
        antesPayees.put(joueurPoker, montant);
    }

    public void ajouterCartesHero(List<CartePoker> cartesHero) throws ErreurLectureFichier {
        if (this.cartesHero != null) throw new ErreurLectureFichier("Cartes hero déjà ajoutées");
        this.cartesHero = cartesHero;
    }

    public void ajouterGains(String joueurPoker, BigDecimal bigDecimal) {
    }

    public void ajouterCartes(String joueurPoker, List<CartePoker> cartePokers) {
    }

    public void calculerLaValueDesActions() {

    }

    public void ajouterStackDepart(String nomJoueur, BigDecimal bigDecimal) {
    }

    public void ajouterBounty(String nomJoueur, BigDecimal bigDecimal) {
    }

    private JoueurPoker obtJoueurParNom(String nomJoueur) throws ErreurLectureFichier {
        if (!joueursPresents.containsKey(nomJoueur)) {
            throw new ErreurLectureFichier("Le joueur n'est pas présent:" + nomJoueur);
        }

        return joueursPresents.get(nomJoueur);
    }
}
