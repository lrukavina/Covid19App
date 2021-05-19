package hr.java.covidportal.model;

public interface Zarazno {
    /**
     * Služi za prelazak zaraze s osobe na osobu s kojom je bila u kontaktu.
     * @param osoba Objekt klase Osoba na koju prelazi bolest (virus).
     */
    void prelazakZarazeNaOsobu(Osoba osoba);
}
