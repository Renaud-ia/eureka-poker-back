package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.util.List;

public class CartesJoueur {
    private final String nomJoueur;
    private final List<CartePoker> cartesJoueurs;

    public CartesJoueur(String nomJoueur, List<CartePoker> cartesJoueurs) {
        this.nomJoueur = nomJoueur;
        this.cartesJoueurs = cartesJoueurs;
    }

    public String obtNomJoueur() {
        return nomJoueur;
    }

    public List<CartePoker> obtCartes() {
        return cartesJoueurs;
    }
}
