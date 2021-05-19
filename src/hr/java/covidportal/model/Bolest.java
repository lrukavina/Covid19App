package hr.java.covidportal.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Služi za unos bolesti preko korisničkog sučelja.
 */
public class Bolest extends ImenovaniEntitet implements Serializable {
    private List<Simptom> simptomi;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(getSimptomi(), bolest.getSimptomi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSimptomi());
    }

    /**
     * Stvara objekt klase Bolest s nazivom i poljem simptoma koje bolest ima.
     * @param naziv Naziv bolesti koje nasljeđuje iz klase <code>ImenovaniEntitet</code>.
     * @param simptomi Polje simptoma koje bolest sadrži.
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv);
        this.simptomi = simptomi;
    }


    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List <Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public String toString() {
        return this.getNaziv();
    }
}
