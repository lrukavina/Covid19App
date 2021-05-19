package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Služi kao tip bolesti (virus) koji je prenosiv drugim osobama.
 */
public class Virus extends Bolest implements Zarazno, Serializable {

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Postavlja naziv i simptome bolesti koji su unešeni preko korisničkog sučelja.
     * @param naziv String koji korisnik postavlja kao naziv bolesti (virusa).
     * @param simptomi Polje objekata klase Simptom koje korisnik postavlja kao simptome bolesti (virusa).
     */
    public Virus(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
    }

    /**
     * Služi za prelazak zaraze s osobe na osobu s kojom je bila u kontaktu.
     * @param osoba Objekt klase Osoba na koju prelazi bolest (virus).
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }
}
