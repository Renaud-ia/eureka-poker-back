package fr.eurekapoker.parties.domaine.parsing.dto;

public class InfosMain {
    private final long identifiantMain;
    public InfosMain(long identifiantMain) {
        this.identifiantMain = identifiantMain;
    }

    public long obtIdentifiantMain() {
        return identifiantMain;
    }
}
