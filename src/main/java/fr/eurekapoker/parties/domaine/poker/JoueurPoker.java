package fr.eurekapoker.parties.domaine.poker;

public class JoueurPoker {
    private final String nomJoueur;
    public JoueurPoker(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }
    public void ajouterResultat(ResultatJoueur resultatJoueur) {
    }

    public String obtNom() {
        return nomJoueur;
    }
}
