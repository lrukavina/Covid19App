package hr.java.covidportal.model;

import org.h2.command.dml.Call;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Služi za unos osoba preko korisničkog sučelja.
 */
public class Osoba {
    private Long id;
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

    /**
     * Služi za kreiranje <b>Pattern Builder</b> klase kako bi unjeli osobu bez dodavanja svih atributa istovremeno.
     */
    public static class Builder{
        private Long id;
        private String ime;
        private String prezime;
        private Date datumRodjenja;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        //konstruktor buildera

        /**
         * Služi za stvaranje novog objekta klase Builder i postavlja mu id.
         * @param id Long koji korisnik postavlja kao id osobe.
         */
        public Builder(Long id){
            this.id = id;
        }

        /**
         * Služi za postavljanje imena objektu
         * @param ime String koji korisnik postavlja kao ime osobe.
         * @return Vraća objekt klase Builder.
         */
        public Builder ime(String ime){
            this.ime = ime;
            return this;
        }

        /**
         * Služi za postavljanje prezimena objektu.
         * @param prezime String koji korisnik postavlja kao prezime osobe.
         * @return Vraća objekt klase Builder.
         */
        public Builder prezime(String prezime){
            this.prezime = prezime;
            return this;
        }

        /**
         * Služi za postavljanje starosti objektu.
         * @param datumRodjenja Sring koji korisnik postavlja kao starost osobe.
         * @return Vraća objekt klase Builder.
         */
        public Builder datumRodjenja(Date datumRodjenja){
            this.datumRodjenja = datumRodjenja;
            return this;
        }

        public Builder starost(Date datumRodjenja){
            this.starost = build().getStarost(datumRodjenja);
            return this;
        }

        /**
         * Služi za postavljanje županije objektu.
         * @param zupanija Objekt klase Županija koji korisnik postavlja kao županiju osobe.
         * @return Vraća objekt klase Builder.
         */
        public Builder zupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        /**
         * Služi za postavljanje bolesti objektu.
         * @param zarazenBolescu Objekt klase Bolest koji korisnik postavlja kao bolest osobe.
         * @return Vraća objekt klase Builder.
         */
        public Builder bolest(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        /**
         * Služi za postavljanje kontaktiranih osoba objektu.
         * @param kontaktiraneOsobe Polje objekata klase Osoba koje korisnik postavlja kao kontaktirane osobe objektu osoba.
         * @return Vraća objekt klase Builder.
         */
        public Builder kontaktiraneOsobe(List<Osoba> kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Builder)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(ime, builder.ime) &&
                    Objects.equals(prezime, builder.prezime) &&
                    Objects.equals(datumRodjenja, builder.datumRodjenja) &&
                    Objects.equals(zupanija, builder.zupanija) &&
                    Objects.equals(zarazenBolescu, builder.zarazenBolescu) &&
                    Objects.equals(kontaktiraneOsobe, builder.kontaktiraneOsobe);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ime, prezime, datumRodjenja, zupanija, zarazenBolescu, kontaktiraneOsobe);
        }

        /**
         * Služi za kreiranje novog objekta klase Osoba bez da se unesu svi parametri koje konstruktor klase Osoba zahtjeva.
         * Prije kreiranja provjerava se je li osoba zaražena virusom, ako je, zarazu prenosi osobama s kojima je bila u kontaktu.
         * @return Vraća objekt klase Osoba.
         */
        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.id = this.id;
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.datumRodjenja = this.datumRodjenja;
            osoba.starost = this.starost;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;

            if(this.zarazenBolescu instanceof Virus && kontaktiraneOsobe != null){
                for(int i = 0; i < this.kontaktiraneOsobe.size(); i++){
                    if(this.kontaktiraneOsobe.get(i) != null){
                        ((Virus) this.zarazenBolescu).prelazakZarazeNaOsobu(this.kontaktiraneOsobe.get(i));
                    }
                }
            }
            return osoba;
        }
    }

    /**
     * Služi kao podrazumjevani konstruktor bez parametara.
     */
    private Osoba(){

    }

    public Integer getStarost(Date datumRodjenja){
        datumRodjenja = this.datumRodjenja;
        Date datumIzracun;
        LocalDate datumSada = LocalDate.now();

        datumIzracun = convertToDateViaInstant(datumSada);

        Calendar godina1 = getCalendar(datumRodjenja);
        Calendar godina2 = getCalendar(datumIzracun);

        int diff = godina1.get(Calendar.YEAR) - godina2.get(Calendar.YEAR);
        if (godina1.get(Calendar.MONTH) > godina2.get(Calendar.MONTH) ||
                (godina1.get(Calendar.MONTH) == godina2.get(Calendar.MONTH) && godina1.get(Calendar.DATE) > godina2.get(Calendar.DATE))) {
            diff--;
        }
        return diff *= -1;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    @Override
    public String toString() {
        return this.ime+" "+this.prezime;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public void setKontaktiranaOsoba(Osoba kontaktiranaOsoba){
        this.kontaktiraneOsobe.add(kontaktiranaOsoba);
    }

    public void setStarost(Integer starost){
        this.starost = starost;
    }

    public Integer getStarost(){
        return starost;
    }
}

