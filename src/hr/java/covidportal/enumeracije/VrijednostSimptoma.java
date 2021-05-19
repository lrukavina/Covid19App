package hr.java.covidportal.enumeracije;

public enum VrijednostSimptoma {

    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("ČESTO"),
    PRODUKTIVNI("Produktivni"),
    INTENZIVNO("Intenzivno"),
    VISOKA("Visoka"),
    JAKA("Jaka");


    private String opis;

    private VrijednostSimptoma(String opis){
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return this.getOpis();
    }
}
