package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.MainPoker;

import java.util.List;

public interface DomaineServiceImport {
    void lancerImport() throws ErreurImport;
    FormatPoker obtFormatPoker() throws ErreurLectureFichier;
    List<MainPoker> obtMainsExtraites();
    List<JoueurPoker> obtJoueursInitiaux();
}
