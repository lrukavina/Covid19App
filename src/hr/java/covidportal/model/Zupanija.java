package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Služi za unos županija preko korisničkog sučelja.
 */
public class Zupanija extends ImenovaniEntitet implements Serializable {
    private Integer brojStanovnika;
    private Integer brojZarazenihOsoba;

    /**
     * Postavlja naziv i broj stanovnika županije koji je unešen preko korisničkog sučelja.
     * @param naziv String koji korisnik unosi kao naziv županije.
     * @param brojStanovnika Integer koji korisnik unosi kao broj stanovnika županije.
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenihOsoba) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenihOsoba = brojZarazenihOsoba;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public Integer getBrojZarazenihOsoba() {return  brojZarazenihOsoba;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(getBrojStanovnika(), zupanija.getBrojStanovnika()) &&
                Objects.equals(getBrojZarazenihOsoba(), zupanija.getBrojZarazenihOsoba());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBrojStanovnika(), getBrojZarazenihOsoba());
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public void setBrojZarazenihOsoba(Integer brojZarazenihOsoba) {this.brojZarazenihOsoba = brojZarazenihOsoba;}

    public int getPostotakZarazenosti(){
        double brojStanovnikaTemp = this.getBrojStanovnika();
        double brojZarazenihTemp = this.brojZarazenihOsoba;
        double postotak = (brojZarazenihTemp/brojStanovnikaTemp) * 100;
        return (int)postotak;
    }

    @Override
    public String toString() {
        return this.getNaziv();
    }
}
