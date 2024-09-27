package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.application.exceptions.PartieNonTrouvee;

public interface PersistanceParties {
    /**
     * @param partiePersistanceDto on veut toujours inclure une main dans une partie même s'il n'y en a qu'une
     */
    void ajouterPartie(PartiePersistanceDto partiePersistanceDto);
    /**
     * permet de récupérer la partie OU LA MAIN avec l'id cherchée
     * @param idPartie l'UUID unique de la partie qu'on cherche
     * @param indexMin l'index de la première main qu'on veut (commence à 1)
     * @param fenetre le nombre de mains qu'on veut
     * @return toujours une PartiePersistenceDTO
     * @throws PartieNonTrouvee lève une exception si partie non trouvée
     */
    PartiePersistanceDto recupererPartie(String idPartie, int indexMin, int fenetre) throws ErreurConsultationPartie;
    void rendreAnonymeJoueurDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto);
    void definirJoueurCentreDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto);
}
