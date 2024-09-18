package fr.eurekapoker.parties.application.imports;

import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;

public interface ConstructeurPersistence extends ObservateurParser {
    String getIdUniquePartie();
    ResumePartieDto obtResumePartie();
    PartiePersistanceDto obtPartie();
}
