package fr.eurekapoker.parties.application.persistance.dto;

import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.UUID;

public class PartiePersistanceDto {
    private String idUniqueGenere;
    private long idParse;
    private boolean joueursAnonymes;
    private String nomRoom;
    private String variante;
    private String typeJeu;
    private String formatSpecialRoom;
    private LocalDateTime date;
    private String nomPartie;
    private BigDecimal buyIn;
    private int nombreSieges;
    private String nomHero;
    private int nombreMains;
    private final List<MainPersistenceDto> mainPersistence;
    private final boolean nombreMainsFixe;

    // constructeur utilisé par persistence
    public PartiePersistanceDto(String idUniqueGenere,
                                long idParse,
                                boolean joueursAnonymes,
                                String nomRoom,
                                String variante,
                                String typeJeu,
                                String formatSpecialRoom,
                                LocalDateTime date,
                                String nomPartie,
                                BigDecimal buyIn,
                                int nombreSieges,
                                int nombreMains) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.joueursAnonymes = joueursAnonymes;
        this.nomRoom = nomRoom;
        this.variante = variante;
        this.typeJeu = typeJeu;
        this.formatSpecialRoom = formatSpecialRoom;
        this.date = date;
        this.nomPartie = nomPartie;
        this.nombreSieges = nombreSieges;
        this.nombreMains = nombreMains;
        this.buyIn = buyIn;
        this.nombreMainsFixe = true;
        this.mainPersistence = new ArrayList<>();
    }

    // utilisé par Constructeur lors du parsing de partie
    public PartiePersistanceDto() {
        this.mainPersistence = new ArrayList<>();
        this.nombreMainsFixe = false;
    }

    public void fixerValeurs(String idUniqueGenere,
                                long idParse,
                                boolean joueursAnonymes,
                                String nomRoom,
                                String variante,
                                String typeJeu,
                                String formatSpecialRoom,
                                LocalDateTime date,
                                String nomPartie,
                                BigDecimal buyIn,
                                int nombreSieges) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.joueursAnonymes = joueursAnonymes;
        this.nomRoom = nomRoom;
        this.variante = variante;
        this.typeJeu = typeJeu;
        this.formatSpecialRoom = formatSpecialRoom;
        this.date = date;
        this.nomPartie = nomPartie;
        this.buyIn = buyIn;
        this.nombreSieges = nombreSieges;
        this.nombreMains = 0;
    }

    public void ajouterMain(MainPersistenceDto mainPersistenceDto) {
        this.mainPersistence.add(mainPersistenceDto);
        // dans le cas où on récupère la partie de la BDD, on veut connaitre le nombre de mains
        // indépendamment de combien on en a récupéré (=pagination)
        if (!nombreMainsFixe) this.nombreMains++;
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

    public void fixNomHero(String nomHero) {
        this.nomHero = nomHero;
    }


    public String obtNomPartie() {
        return nomPartie;
    }

    public String obtNomRoom() {
        return nomRoom;
    }

    public String obtVariante() {
        return variante;
    }

    public String obtTypeTable() {
        return typeJeu;
    }

    public long obtIdParse() {
        return idParse;
    }

    public String obtNomHero() {
        return nomHero;
    }

    public LocalDateTime obtDate() {
        return date;
    }

    public String obtFormatSpecialRoom() {
        return formatSpecialRoom;
    }

    public int obtNombreMains() {
        return nombreMains;
    }

    public boolean obtJoueursAnonymes() {
        return joueursAnonymes;
    }

    public BigDecimal obtBuyIn() {
        return buyIn;
    }

    public void partieTerminee() {
        this.nombreMains++;
        rendreImmuablesValeurs();
    }
}
