package fr.eurekapoker.parties.application.api.dto;

public class ParametresImport {
    private boolean joueursAnonymes;

    public ParametresImport() {

    }
    public ParametresImport(boolean joueursAnonymes) {
        this.joueursAnonymes = joueursAnonymes;
    }

    public boolean getJoueursAnonymes() {
        return joueursAnonymes;
    }

    public void setJoueursAnonymes(boolean joueursAnonymes) {
        this.joueursAnonymes = joueursAnonymes;
    }
}
