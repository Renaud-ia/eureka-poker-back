package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
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
    public void rendreAnonymeJoueurDansPartie(String idPartie, String nomJoueur)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur);
        persistanceParties.rendreAnonymeJoueurDansPartie(idPartie, joueurPersistenceDto);
    }

    @Override
    public void definirHeroDansPartie(String idPartie, String nomJoueur)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur);
        persistanceParties.definirHeroDansPartie(idPartie, joueurPersistenceDto);
    }
}
