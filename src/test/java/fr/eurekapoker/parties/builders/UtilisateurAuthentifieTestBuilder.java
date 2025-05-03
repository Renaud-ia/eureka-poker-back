package fr.eurekapoker.parties.builders;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;

import java.util.UUID;

public class UtilisateurAuthentifieTestBuilder {
    private String idUtilisateur = UUID.randomUUID().toString();
    private String emailUtilisateur = "test@eurekapoker.fr";
    private boolean emailVerifie = true;
    private UtilisateurAuthentifie.StatutMembre statutMembre = UtilisateurAuthentifie.StatutMembre.UTILISATEUR;
    private String nomFamille = "Dupont";
    private String prenom = "Jean";

    public static UtilisateurAuthentifieTestBuilder unUtilisateurAuthentifie() {
        return new UtilisateurAuthentifieTestBuilder();
    }

    public UtilisateurAuthentifieTestBuilder avecId(String id) {
        this.idUtilisateur = id;
        return this;
    }

    public UtilisateurAuthentifieTestBuilder avecEmail(String email) {
        this.emailUtilisateur = email;
        return this;
    }

    public UtilisateurAuthentifieTestBuilder avecEmailVerifie(boolean verifie) {
        this.emailVerifie = verifie;
        return this;
    }

    public UtilisateurAuthentifieTestBuilder avecStatut(UtilisateurAuthentifie.StatutMembre statut) {
        this.statutMembre = statut;
        return this;
    }

    public UtilisateurAuthentifieTestBuilder avecNom(String nom) {
        this.nomFamille = nom;
        return this;
    }

    public UtilisateurAuthentifieTestBuilder avecPrenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public UtilisateurAuthentifie build() {
        return new UtilisateurAuthentifie(
                idUtilisateur,
                emailUtilisateur,
                emailVerifie,
                statutMembre,
                nomFamille,
                prenom
        );
    }
}
