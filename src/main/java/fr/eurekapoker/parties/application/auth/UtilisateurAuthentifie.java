package fr.eurekapoker.parties.application.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class UtilisateurAuthentifie {
    private final String idUtilisateur;
    private final String emailUtilisateur;
    private final boolean emailVerifie;
    private final StatutMembre statutMembre;
    private final String nomFamille;
    private final String prenom;

    public enum StatutMembre {
        TEMPORAIRE,
        UTILISATEUR,
        ADMIN,
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof UtilisateurAuthentifie that)) return false;

        return Objects.equals(this.idUtilisateur, that.idUtilisateur);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idUtilisateur);
    }
}
