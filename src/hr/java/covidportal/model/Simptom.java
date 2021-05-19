package hr.java.covidportal.model;

import hr.java.covidportal.enumeracije.VrijednostSimptoma;

import java.io.Serializable;
import java.util.Objects;

/**
 * Služi za unos simptoma preko korisničkog sučelja.
 */
public class Simptom extends ImenovaniEntitet implements Serializable {
    private VrijednostSimptoma vrijednost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return getVrijednost() == simptom.getVrijednost();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVrijednost());
    }


    /**
     * Postavlja naziv i vrijednost simptoma koji je unešen preko korisničkog sučelja.
     * @param naziv String koji korisnik unosi kao naziv simptoma.
     * @param vrijednost String koji korisnik unosi kao vrijednost simptoma.
     */
    public Simptom(Long id, String naziv, VrijednostSimptoma vrijednost) {
        super(id, naziv);
        this.vrijednost = vrijednost;
    }

    public VrijednostSimptoma getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(VrijednostSimptoma vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public String toString() {
        return this.getNaziv()+" "+this.getVrijednost().getOpis();
    }
}
