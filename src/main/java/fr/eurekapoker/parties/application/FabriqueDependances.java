package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.domaine.poker.moteur.EncodageSituation;

public class FabriqueDependances {
    public static ConstructeurPersistence obtConstructeurPersistance() {
        return new ConstructeurPersistenceDto(new EncodageSituation());
    }
}
