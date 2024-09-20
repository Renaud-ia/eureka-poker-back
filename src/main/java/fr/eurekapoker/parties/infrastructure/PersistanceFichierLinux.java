package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;

import java.time.LocalDateTime;

public class PersistanceFichierLinux implements PersistanceFichiers {
    @Override
    public void enregistrerFichier(String idUniqueGenere, String contenuFichier) {

    }

    @Override
    public String recupererFichier(String idUniqueGenere, LocalDateTime dateSauvegarde) {
        return "";
    }
}
