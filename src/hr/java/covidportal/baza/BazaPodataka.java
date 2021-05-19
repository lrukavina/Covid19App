package hr.java.covidportal.baza;

import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class BazaPodataka {

    private static final String FILECONFIG = "src/hr/java/covidportal/baza/config.properties";

    private static Connection otvoriVezuNaBazu() {
        Properties properties = new Properties();
        Connection veza = null;

        try {
            properties.load(new FileReader(FILECONFIG));

            String urlBaze = properties.getProperty("urlBazePodataka");
            String korisnickoIme = properties.getProperty("korisnickoIme");
            String lozinka = properties.getProperty("lozinka");

            veza = DriverManager.getConnection(urlBaze, korisnickoIme, lozinka);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return veza;
    }

    private static void zatvoriVezuNaBazu(Connection veza) {
        try {
            veza.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Set<Simptom> dohvatiSveSimptomeIzBaze() throws SQLException, IOException {
        Set<Simptom> simptomi = new HashSet<>();

        Connection veza = otvoriVezuNaBazu();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        while (rs.next()) {
            long id = rs.getLong("id");
            String naziv = rs.getString("naziv");
            String vrijednostSimptoma = rs.getString("vrijednost");

            VrijednostSimptoma vrijednostSimptomaEnum = null;
            switch (vrijednostSimptoma) {
                case "ČESTO":
                    vrijednostSimptomaEnum = VrijednostSimptoma.CESTO;
                    break;
                case "SREDNJE":
                    vrijednostSimptomaEnum = VrijednostSimptoma.SREDNJE;
                    break;
                case "RIJETKO":
                    vrijednostSimptomaEnum = VrijednostSimptoma.RIJETKO;
                    break;
                case "Produktivni":
                    vrijednostSimptomaEnum = VrijednostSimptoma.PRODUKTIVNI;
                    break;
                case "Intenzivno":
                    vrijednostSimptomaEnum = VrijednostSimptoma.INTENZIVNO;
                    break;
                case "Visoka":
                    vrijednostSimptomaEnum = VrijednostSimptoma.VISOKA;
                    break;
                case "Jaka":
                    vrijednostSimptomaEnum = VrijednostSimptoma.JAKA;
                    break;
            }

            Simptom noviSimptom = new Simptom(id, naziv, vrijednostSimptomaEnum);
            simptomi.add(noviSimptom);
        }

        zatvoriVezuNaBazu(veza);
        return simptomi;
    }

    public static Simptom dohvatiJedanSimptomIzBaze(Long id) throws SQLException, IOException {
        Simptom noviSimptom = null;

        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID= ?");
        upit.setLong(1, id);
        ResultSet rs = upit.executeQuery();

        while (rs.next()) {
            long idBaza = rs.getLong("id");
            String naziv = rs.getString("naziv");
            String vrijednostSimptoma = rs.getString("vrijednost");

            VrijednostSimptoma vrijednostSimptomaEnum = null;
            switch (vrijednostSimptoma) {
                case "ČESTO":
                    vrijednostSimptomaEnum = VrijednostSimptoma.CESTO;
                    break;
                case "SREDNJE":
                    vrijednostSimptomaEnum = VrijednostSimptoma.SREDNJE;
                    break;
                case "RIJETKO":
                    vrijednostSimptomaEnum = VrijednostSimptoma.RIJETKO;
                    break;
                case "Produktivni":
                    vrijednostSimptomaEnum = VrijednostSimptoma.PRODUKTIVNI;
                    break;
                case "Intenzivno":
                    vrijednostSimptomaEnum = VrijednostSimptoma.INTENZIVNO;
                    break;
                case "Visoka":
                    vrijednostSimptomaEnum = VrijednostSimptoma.VISOKA;
                    break;
                case "Jaka":
                    vrijednostSimptomaEnum = VrijednostSimptoma.JAKA;
                    break;
            }

            noviSimptom = new Simptom(idBaza, naziv, vrijednostSimptomaEnum);
        }

        zatvoriVezuNaBazu(veza);
        return noviSimptom;
    }

    public static void spremiNoviSimptom(Simptom noviSimptom) throws SQLException, IOException {

        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO SIMPTOM(id, naziv, vrijednost) VALUES (?, ?, ?)");

        upit.setLong(1, noviSimptom.getId());
        upit.setString(2, noviSimptom.getNaziv());
        upit.setString(3, noviSimptom.getVrijednost().getOpis());

        upit.executeUpdate();

        zatvoriVezuNaBazu(veza);
    }

    public static Set<Bolest> dohvatiSveBolestiIzBaze() throws SQLException, IOException {
        Set<Bolest> bolesti = new HashSet<>();
        List<Simptom> simptomi = new ArrayList<>();


        Connection veza = otvoriVezuNaBazu();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");

        while(rs.next()){
            long idBolesti = rs.getLong("id");
            String nazivBolesti = rs.getString("naziv");
            Boolean virus = rs.getBoolean("virus");

            PreparedStatement upit = veza.prepareStatement("select simptom.*\n" +
                    "from bolest_simptom inner join\n" +
                    "        simptom on simptom.id = bolest_simptom.simptom_id\n" +
                    "where bolest_simptom.bolest_id = ?");

            upit.setLong(1, idBolesti);
            ResultSet rs2 = upit.executeQuery();

            while (rs2.next()){
                long idSimptoma = rs2.getLong("id");
                String nazivSimptoma = rs2.getString("naziv");
                String vrijednostSimptoma = rs2.getString("vrijednost");

                VrijednostSimptoma vrijednostSimptomaEnum = null;
                switch (vrijednostSimptoma) {
                    case "ČESTO":
                        vrijednostSimptomaEnum = VrijednostSimptoma.CESTO;
                        break;
                    case "SREDNJE":
                        vrijednostSimptomaEnum = VrijednostSimptoma.SREDNJE;
                        break;
                    case "RIJETKO":
                        vrijednostSimptomaEnum = VrijednostSimptoma.RIJETKO;
                        break;
                    case "Produktivni":
                        vrijednostSimptomaEnum = VrijednostSimptoma.PRODUKTIVNI;
                        break;
                    case "Intenzivno":
                        vrijednostSimptomaEnum = VrijednostSimptoma.INTENZIVNO;
                        break;
                    case "Visoka":
                        vrijednostSimptomaEnum = VrijednostSimptoma.VISOKA;
                        break;
                    case "Jaka":
                        vrijednostSimptomaEnum = VrijednostSimptoma.JAKA;
                        break;
                }

                Simptom simptomBolesti = new Simptom(idSimptoma, nazivSimptoma, vrijednostSimptomaEnum);
                simptomi.add(simptomBolesti);
            }

            List<Simptom> simptomiZaZapis = new ArrayList<>();
            simptomiZaZapis.addAll(simptomi);
            if(virus){
                Virus noviVirus = new Virus(idBolesti, nazivBolesti, simptomiZaZapis);
                bolesti.add(noviVirus);
            }
            else{
                Bolest novaBolest = new Bolest(idBolesti, nazivBolesti, simptomiZaZapis);
                bolesti.add(novaBolest);
            }

            simptomi.clear();

        }

        zatvoriVezuNaBazu(veza);
        return bolesti;
    }

    public static Bolest dohvatiJednuBolestIzBaze(Long id) throws SQLException, IOException {
        Set<Bolest> bolesti = new HashSet<>();
        Bolest novaBolest = null;

        Connection veza = otvoriVezuNaBazu();

        bolesti = dohvatiSveBolestiIzBaze();

        for (Bolest bolest : bolesti) {
            if (bolest.getId().equals(id)) {
                novaBolest = bolest;
            }
        }

        zatvoriVezuNaBazu(veza);
        return novaBolest;
    }

    public static void spremiNovuBolest(Bolest novaBolest) throws SQLException, IOException {

        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO BOLEST(id, naziv, virus) VALUES (?, ?, ?)");

        upit.setLong(1, novaBolest.getId());
        upit.setString(2, novaBolest.getNaziv());
        if (novaBolest instanceof Virus) {
            upit.setString(3, "TRUE");
        } else {
            upit.setString(3, "FALSE");
        }

        upit.executeUpdate();

        for (Simptom simptom : novaBolest.getSimptomi()) {
            upit = veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(bolest_id, simptom_id) VALUES(?, ?)");

            upit.setLong(1, novaBolest.getId());
            upit.setLong(2, simptom.getId());

            upit.executeUpdate();
        }


        zatvoriVezuNaBazu(veza);
    }

    public static List<Zupanija> dohvatiSveZupanijeIzBaze() throws SQLException, IOException {
        List<Zupanija> zupanije = new ArrayList<>();

        Connection veza = otvoriVezuNaBazu();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        while (rs.next()) {
            long id = rs.getLong("id");
            String naziv = rs.getString("naziv");
            Integer brojStanovnika = rs.getInt("broj_stanovnika");
            Integer brojZarazenihStanovnika = rs.getInt("broj_zarazenih_stanovnika");

            Zupanija novaZupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenihStanovnika);
            zupanije.add(novaZupanija);
        }

        zatvoriVezuNaBazu(veza);
        return zupanije;
    }

    public static Zupanija dohvatiJednuZupanijuIzBaze(Long id) throws SQLException, IOException {
        Zupanija novaZupanija = null;

        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID= ?");
        upit.setLong(1, id);
        ResultSet rs = upit.executeQuery();

        while (rs.next()) {
            long idZupanije = rs.getLong("id");
            String naziv = rs.getString("naziv");
            Integer brojStanovnika = rs.getInt("broj_stanovnika");
            Integer brojZarazenihStanovnika = rs.getInt("broj_zarazenih_stanovnika");

            novaZupanija = new Zupanija(idZupanije, naziv, brojStanovnika, brojZarazenihStanovnika);
        }

        zatvoriVezuNaBazu(veza);
        return novaZupanija;
    }

    public static void spremiNovuZupaniju(Zupanija novaZupanija) throws SQLException, IOException{

        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO ZUPANIJA(id, naziv, broj_stanovnika, broj_zarazenih_stanovnika) VALUES (?, ?, ?, ?)");

        upit.setLong(1, novaZupanija.getId());
        upit.setString(2, novaZupanija.getNaziv());
        upit.setInt(3, novaZupanija.getBrojStanovnika());
        upit.setInt(4, novaZupanija.getBrojZarazenihOsoba());

        upit.executeUpdate();

        zatvoriVezuNaBazu(veza);
    }

    public static List<Osoba> dohvatiSveOsobeIzBaze() throws SQLException, IOException{
        List<Zupanija> zupanije = dohvatiSveZupanijeIzBaze();
        Set<Bolest> bolesti = dohvatiSveBolestiIzBaze();
        List<Osoba> osobe = new ArrayList<>();

        Zupanija zupanijaOsobe = null;
        Bolest bolestOsobe = null;

        Connection veza = otvoriVezuNaBazu();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");

        while(rs.next()){
            Long id = rs.getLong("id");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            Date datumRodjenja = rs.getDate("datum_rodjenja");
            Long zupanijaId = rs.getLong("zupanija_id");
            Long bolesId = rs.getLong("bolest_id");

            for(Zupanija zupanija:zupanije){
                if(zupanija.getId().equals(zupanijaId)){
                    zupanijaOsobe = zupanija;
                }
            }

            for(Bolest bolest:bolesti){
                if(bolest.getId().equals(bolesId)){
                    bolestOsobe = bolest;
                }
            }


            Osoba novaOsoba = new Osoba.Builder(id)
                    .ime(ime)
                    .prezime(prezime)
                    .datumRodjenja(datumRodjenja)
                    .starost(datumRodjenja)
                    .zupanija(zupanijaOsobe)
                    .bolest(bolestOsobe)
                    .build();

            osobe.add(novaOsoba);
        }

        stmt = veza.createStatement();
        rs = stmt.executeQuery("SELECT * FROM KONTAKTIRANE_OSOBE");

        List<Long> osobaId = new ArrayList<>();
        List<Long> kontaktiraneOsobeId = new ArrayList<>();
        while(rs.next()){
            osobaId.add(rs.getLong("osoba_id"));
            kontaktiraneOsobeId.add(rs.getLong("kontaktirana_osoba_id") - 1);

        }

       for(int i = 0; i < osobe.size(); i++){
           for(int j = 0; j < osobaId.size(); j++){
               if(osobaId.get(j).equals(osobe.get(i).getId())){
                   osobe.get(i).setKontaktiranaOsoba(osobe.get(Math.toIntExact(kontaktiraneOsobeId.get(j))));
               }
           }
       }

        zatvoriVezuNaBazu(veza);
        return osobe;
    }

    public static Osoba dohvatiJednuOsobuIzBaze(Long id) throws SQLException, IOException{

        Zupanija zupanijaOsobe = null;
        Bolest bolestOsobe = null;
        Osoba novaOsoba = null;
        List<Osoba> osobe = new ArrayList<>();
        List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        osobe.addAll(dohvatiSveOsobeIzBaze());


        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID= ?");
        upit.setLong(1, id);
        ResultSet rs = upit.executeQuery();

        while (rs.next()) {
            long idOsobe = rs.getLong("id");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            Date datumRodjenja = rs.getDate("datum_rodjenja");
            Long idZupanije = rs.getLong("zupanija_id");
            Long idBolesti = rs.getLong("bolest_id");

            zupanijaOsobe = dohvatiJednuZupanijuIzBaze(idZupanije);
            bolestOsobe = dohvatiJednuBolestIzBaze(idBolesti);

            novaOsoba = new Osoba.Builder(id)
                    .ime(ime)
                    .prezime(prezime)
                    .datumRodjenja(datumRodjenja)
                    .starost(datumRodjenja)
                    .zupanija(zupanijaOsobe)
                    .bolest(bolestOsobe)
                    .build();
        }

        upit = veza.prepareStatement("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID= ?");
        upit.setLong(1, id);
        rs = upit.executeQuery();

        while(rs.next()){
            long idOsobe = rs.getLong("osoba_id");
            long idKontaktiraneOsobe = rs.getLong("kontaktirana_osoba_id");

            for(Osoba osoba:osobe){
                if(idOsobe == osoba.getId()){
                    kontaktiraneOsobe.add(osobe.get((int) idKontaktiraneOsobe));
                }
            }
        }

        if(novaOsoba != null){
            novaOsoba.setKontaktiraneOsobe(kontaktiraneOsobe);
        }

        zatvoriVezuNaBazu(veza);
        return novaOsoba;

    }

    public static void spremiNovuOsobu(Osoba novaOsoba) throws SQLException, IOException{
        Connection veza = otvoriVezuNaBazu();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO OSOBA(id, ime, prezime, datum_rodjenja, zupanija_id, bolest_id) VALUES (?, ?, ?, ?, ?, ?)");

        upit.setLong(1, novaOsoba.getId());
        upit.setString(2, novaOsoba.getIme());
        upit.setString(3, novaOsoba.getPrezime());
        upit.setDate(4, (java.sql.Date) novaOsoba.getDatumRodjenja());
        upit.setLong(5, novaOsoba.getZupanija().getId());
        upit.setLong(6, novaOsoba.getZarazenBolescu().getId());

        upit.executeUpdate();

        upit = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(osoba_id, kontaktirana_osoba_id) VALUES (?, ?)");

        for(int i = 0; i < novaOsoba.getKontaktiraneOsobe().size(); i++){
            upit.setLong(1, novaOsoba.getId());
            upit.setLong(2, novaOsoba.getKontaktiraneOsobe().get(i).getId());

            upit.executeUpdate();
        }

        zatvoriVezuNaBazu(veza);
    }
}
