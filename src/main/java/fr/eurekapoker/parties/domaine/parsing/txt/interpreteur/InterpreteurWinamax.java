package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

public class InterpreteurWinamax implements InterpreteurLigne {
    // todo
    @Override
    public boolean estFormat(String ligne) {
        return false;
    }

    @Override
    public boolean estNouvelleMain(String ligne) {
        return false;
    }

    @Override
    public boolean estJoueur(String ligne) {
        return false;
    }

    @Override
    public boolean estNouveauTour(String ligne) {
        return false;
    }

    @Override
    public boolean estGain(String ligne) {
        return false;
    }

    @Override
    public boolean estAction(String ligne) {
        return false;
    }

    @Override
    public boolean estBlindeAnte(String ligne) {
        return false;
    }
}
