package hr.java.covidportal.genericsi;

import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Virus;

import java.util.List;

public class KlinikaZaInfektivneBolesti<T extends  Virus,S extends Osoba> {
    private List<T> virusi;
    private List<S> osobe;

    public KlinikaZaInfektivneBolesti(List<T> virusi, List<S> osobe) {
        this.virusi = virusi;
        this.osobe = osobe;
    }

    @Override
    public String toString() {
        return "KlinikaZaInfektivneBolesti{" +
                "virusi=" + virusi +
                ", osobe=" + osobe +
                '}';
    }

    public List<T> getVirusi() {
        return virusi;
    }

    public void setVirusi(List<T> virusi) {
        this.virusi = virusi;
    }

    public void addVirus(T virus){
        this.virusi.add(virus);
    }

    public List<S> getOsobe() {
        return osobe;
    }

    public void setOsobe(List<S> osobe) {
        this.osobe = osobe;
    }

    public void addOsoba(S osoba){
        this.osobe.add(osoba);
    }
}
