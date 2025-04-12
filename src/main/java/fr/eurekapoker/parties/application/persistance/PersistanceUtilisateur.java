package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;

public interface PersistanceUtilisateur {
    UtilisateurAuthentifie trouverOuCreer(UtilisateurAuthentifie utilisateurAuthentifie);
}
