package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.persistance.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;

public class InterfacePartiesImpl implements InterfaceParties {
    private final PersistanceParties persistanceParties;
    public InterfacePartiesImpl(PersistanceParties persistanceParties) {
        this.persistanceParties = persistanceParties;
    }
    @Override
    public ResumePartieDto ajouterPartie(String contenuPartie) throws ErreurAjoutPartie {
        return null;
    }

    @Override
    public ContenuPartieDto consulterMainsParties(String idPartie, int indexPremiereMain, int nombreMains)
            throws ErreurConsultationPartie {
        return null;
    }

    @Override
    public void rendreAnonymeJoueurDansPartie(String idPartie, String nomJoueur, String room)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur, room);
        long idJoueur = persistanceParties.ajouterOuRecupererJoueur(joueurPersistenceDto);
        persistanceParties.rendreAnonymeJoueurDansPartie(idPartie, idJoueur);
    }

    @Override
    public void definirHeroDansPartie(String idPartie, String nomJoueur, String room)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur, room);
        long idJoueur = persistanceParties.ajouterOuRecupererJoueur(joueurPersistenceDto);
        persistanceParties.definirHeroDansPartie(idPartie, idJoueur);
    }
}
