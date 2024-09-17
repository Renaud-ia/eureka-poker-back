package fr.eurekapoker.parties.application.persistance;

import java.time.LocalDateTime;

public interface PersistanceFichiers {
    void enregistrerFichier(String idUniqueGenere,
                            String contenuFichier,
                            String extensionFichier);

    String recupererFichier(String idUniqueGenere, LocalDateTime dateSauvegarde);
}
