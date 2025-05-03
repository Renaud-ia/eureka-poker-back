package fr.eurekapoker.parties.builders;
import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;

import java.util.UUID;

public class UtilisateurIdentifieTestBuilder {

    private UtilisateurAuthentifie utilisateurAuthentifie = UtilisateurAuthentifieTestBuilder
            .unUtilisateurAuthentifie()
            .build();

    private String identifiantSession = UUID.randomUUID().toString();

    public static UtilisateurIdentifieTestBuilder unUtilisateurIdentifie() {
        return new UtilisateurIdentifieTestBuilder();
    }

    public UtilisateurIdentifieTestBuilder avecUtilisateurAuthentifie(UtilisateurAuthentifie utilisateur) {
        this.utilisateurAuthentifie = utilisateur;
        return this;
    }

    public UtilisateurIdentifieTestBuilder avecIdentifiantSession(String sessionId) {
        this.identifiantSession = sessionId;
        return this;
    }

    public UtilisateurIdentifie build() {
        return new UtilisateurIdentifie(utilisateurAuthentifie, identifiantSession);
    }
}