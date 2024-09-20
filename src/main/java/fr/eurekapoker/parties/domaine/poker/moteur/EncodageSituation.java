package fr.eurekapoker.parties.domaine.poker.moteur;


import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.utils.Bits;

import java.util.LinkedList;

/**
 * produit un identifiant unique par action (de type LONG)
 * labellise pendant l'import
 * à partir de l'identifiant regénère la situation avec les infos : nombre de joueurs, position etc
 * ne prend pas en compte le cas où SB est absente (= marginal) mais va le transposer
 * compare les distances entre séquences d'action -> permet de mapper des séquences
 * non existantes dans l'arbre Abstrait vers la situation la plus proche
 * Construction :
 * 1) par initialisation puis ajout action
 * 2) par id Long
 * IMPORTANT : pour cohérence des actions ALL-IN = ALL-IN
 * détections incorrectes des LEAFS avec ALL-IN car dépend des différents stacks
 */
public class EncodageSituation {
    public static final int MAX_JOUEURS = 12;
    private TourPoker.RoundPoker round;
    private int nombreJoueursInitiaux;

    private LinkedList<ActionPoker.TypeAction> suiteMoves;

    private final static int N_BITS_MOVE = Bits.bitsNecessaires(ActionPoker.TypeAction.values().length);
    private final static int N_BITS_ROUND = Bits.bitsNecessaires(TourPoker.RoundPoker.values().length);
    private final static int N_BITS_JOUEURS = Bits.bitsNecessaires(MAX_JOUEURS);

    public final static int MAX_ACTIONS = (Long.SIZE - N_BITS_JOUEURS - N_BITS_ROUND) / N_BITS_MOVE;

    //constructeurs

    public EncodageSituation(int joueursInitiaux, TourPoker.RoundPoker round) {
        initialiserNoeud(joueursInitiaux, round);
    }

    public EncodageSituation(long idUnique) throws Exception {
        // masques pour extraire les informations
        int maskJoueurs = ((1 << N_BITS_JOUEURS) - 1) << N_BITS_ROUND;
        int maskRound = (1 << N_BITS_ROUND) - 1;

        int joueursInitiaux = (int) ((idUnique & maskJoueurs) >> N_BITS_ROUND);
        int intRound = (int) (idUnique & maskRound);

        initialiserNoeud(joueursInitiaux, TourPoker.RoundPoker.values()[intRound]);

        long actions = idUnique >> (N_BITS_ROUND + N_BITS_JOUEURS);

        // ensuite on ajoute les actions
        int maskMove = (1 << N_BITS_MOVE) - 1;
        while (actions > 0) {
            ActionPoker.TypeAction move = (ActionPoker.TypeAction.values()[(int) (actions & maskMove) - 1]);
            this.ajouterAction(move);
            actions = actions >> N_BITS_MOVE;
        }
    }

    private void initialiserNoeud(int joueursInitiaux, TourPoker.RoundPoker round) {
        this.nombreJoueursInitiaux = joueursInitiaux;
        this.round = round;
        this.suiteMoves = new LinkedList<>();
    }

    public void ajouterAction(ActionPoker.TypeAction move) throws Exception {
        if (this.suiteMoves.size() == MAX_ACTIONS)
            throw new Exception("Le nombre de moves dépassent le maximum admis");
        this.suiteMoves.addFirst(move);
    }

    public long toLong() {
        long longAction = 0L;
        int bitsAjoutes = 0;

        for (ActionPoker.TypeAction move : suiteMoves) {
            // important on rajoute 1 pour ne pas avoir zéro
            longAction = (longAction << N_BITS_MOVE) | (move.ordinal() + 1);
            bitsAjoutes += N_BITS_MOVE;
        }

        // IMPORTANT => les actions sont au début donc permet de trier rapidement les actions par valeur du long
        // si on déborde on genère -1 => noeud invalide
        int CAPACITE_LONG = 64;
        if ((bitsAjoutes + N_BITS_ROUND + N_BITS_JOUEURS) >= CAPACITE_LONG) {
            throw new RuntimeException("Trop d'actions dans le code");
        }

        // on met joueurs initiaux en premier car jamais nul
        longAction = (longAction << N_BITS_JOUEURS) | this.nombreJoueursInitiaux;
        longAction = (longAction << N_BITS_ROUND) | round.ordinal();

        return longAction;
    }
}

