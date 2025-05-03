package fr.eurekapoker.parties.application.auth;

import fr.eurekapoker.parties.application.exceptions.ErreurAuthentification;
import lombok.Getter;
import java.util.Objects;

public class UtilisateurIdentifie {
    @Getter
    private final UtilisateurAuthentifie utilisateurAuthentifie;
    @Getter
    private final String identifiantSession;

    public UtilisateurIdentifie(UtilisateurAuthentifie utilisateurAuthentifie, String identifiantSession) {
        if (utilisateurAuthentifie == null && identifiantSession == null) {
            throw new ErreurAuthentification("L'utilisateur n'est pas identifié");
        }

        this.utilisateurAuthentifie = utilisateurAuthentifie;
        this.identifiantSession = identifiantSession;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof UtilisateurIdentifie)) return false;

        UtilisateurAuthentifie autreUtilisateur = ((UtilisateurIdentifie) other).utilisateurAuthentifie;
        if (autreUtilisateur != null) {
            return autreUtilisateur.equals(this.utilisateurAuthentifie);
        }

        String autreIdentifiantSession = ((UtilisateurIdentifie) other).identifiantSession;

        if (autreIdentifiantSession == null) {
            return false;
        }

        return Objects.equals(this.identifiantSession, autreIdentifiantSession);
    }

    @Override
    public int hashCode() {
        return utilisateurAuthentifie != null
                ? utilisateurAuthentifie.hashCode()
                : Objects.hashCode(identifiantSession);
    }


}
