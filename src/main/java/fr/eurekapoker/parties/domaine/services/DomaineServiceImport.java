package fr.eurekapoker.parties.domaine.services;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;

public interface DomaineServiceImport {
    void lancerImport() throws ErreurImport;
}
