package fr.eurekapoker.parties.application.persistance.dto;

import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.UUID;

public class PartiePersistanceDto {
    private final String idUniqueGenere;
    private final long idParse;
    private final String nomRoom;
    private final String formatPoker;
    private final String typeJeu;
    private final LocalDateTime date;
    private final String nomPartie;
    private final int nombreSieges;
    private int nombreMains;
    private final List<MainPersistenceDto> mainPersistence;

    public PartiePersistanceDto(String idUniqueGenere,
                                long idParse,
                                String nomRoom,
                                String formatPoker,
                                String typeJeu,
                                LocalDateTime date,
                                String nomPartie,
                                int nombreSieges) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.nomRoom = nomRoom;
        this.formatPoker = formatPoker;
        this.typeJeu = typeJeu;
        this.date = date;
        this.nomPartie = nomPartie;
        this.nombreSieges = nombreSieges;
        this.nombreMains = 0;
        this.mainPersistence = new ArrayList<>();
    }

    public void ajouterMain(MainPersistenceDto mainPersistenceDto) {
        this.mainPersistence.add(mainPersistenceDto);
        this.nombreMains++;
    }

    public List<MainPersistenceDto> obtMains() {
        return mainPersistence;
    }

    public int obtNombreSieges() {
        return nombreSieges;
    }

    public String obtIdUnique() {
        return idUniqueGenere;
    }

    public void rendreImmuablesValeurs() {
        // todo empeche la modification des valeurs
    }


}
