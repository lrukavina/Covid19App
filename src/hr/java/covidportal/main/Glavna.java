package hr.java.covidportal.main;

import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import hr.java.covidportal.model.*;

import hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class Glavna {

    private static final Logger Logger = LoggerFactory.getLogger(Glavna.class);
    static final String FILEZUPANIJE = "dat/zupanije.txt";
    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEBOLESTI = "dat/bolesti.txt";
    static final String FILEVIRUSI = "dat/virusi.txt";
    static final String FILEOSOBE = "dat/osobe.txt";
    static final String FILESERIJALIZACIJA = "dat/serijalizacijaZupanija.dat";

    /**
     * Služi kao korisničko sučelje kojim korisnik unosi županije, simptome, bolesti i osobe.
     * Korisnik na kraju unosa ima uvid u sve što je prethodno unio.
     *
     * @param args argumenti komandne linije (ne koriste se).
     */
    public static void main(String args[]){

        Logger.info("Aplikacija je pokrenuta.");

        Scanner unos = new Scanner(System.in);



        List<Zupanija> zupanije = new ArrayList<>();
        Set <Simptom> simptomi = new HashSet<>();
        Set <Bolest> bolesti = new HashSet<>();
        Map <Bolest, List<Osoba>> mapaOboljelihOsoba = new HashMap<>();
        List<Osoba> osobe = new ArrayList<>();


        //unosi
        unosZupanija(zupanije);
        unosSimptoma(simptomi);
        unosBolesti(bolesti, simptomi);
        unosVirusa(bolesti, simptomi);
        unosOsoba(zupanije, bolesti, osobe);
        serializirajZupanije(zupanije);

        KlinikaZaInfektivneBolesti klinikaZaInfektivneBolesti = napuniKlinikuZaInfektivneBolesti(bolesti, osobe);
        popisOsoba(osobe);
        mapirajOboljeleOsobe(mapaOboljelihOsoba, osobe);
        ispisiOboljeleOsobe(mapaOboljelihOsoba, bolesti);
        ispisZupanijeSNajvecimPostotkomOboljelih(zupanije);

        Instant pocetakZaLambdu = Instant.now();
        sortirajVirusePomocuLambde(bolesti, klinikaZaInfektivneBolesti);
        Instant krajZaLambdu = Instant.now();
        Long trajanjeZaLambdu = (Duration.between(pocetakZaLambdu, krajZaLambdu).toMillis());

        List<Bolest> sortiraniVirusi = klinikaZaInfektivneBolesti.getVirusi();
        Integer indeks = 0;
        System.out.println("Virusi sortirani po nazivu suprotno od poretka abecede:");
        for(Bolest bolest:sortiraniVirusi){
            System.out.println((++indeks)+". "+bolest.getNaziv());
        }

        Instant pocetakBezLambde = Instant.now();
        sortirajViruseBezLambde(bolesti, klinikaZaInfektivneBolesti);
        Instant krajBezLambde = Instant.now();
        Long trajanjeBezLambde = (Duration.between(pocetakBezLambde, krajBezLambde).toMillis());

        System.out.println("Sortiranje objekata korištenjem lambdi traje "+trajanjeZaLambdu +" milisekundi, a bez lambdi traje "+
                trajanjeBezLambde +" milisekundi.");

        filtrirajPoStringu(unos, osobe);
        mapirajSimptomeBolesti(bolesti);

        Logger.info("Aplikacija je završila s radom.");

    }

    /**
     * Služi za unos županije, te tako unešenu županiju vraća kao privremeni objekt županije u main metodu gdje ju sprema u polje županija.
     * @return Vraća privremeni objekt klase Županija.
     */
    private static List<Zupanija> unosZupanija(List<Zupanija> zupanije) {
        System.out.println("Učitavanje podataka o županijama...");
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILEZUPANIJE), Charset.forName("UTF-8")))){
            String linija = null;
            while((linija = input.readLine()) != null){
                Long id = Long.parseLong(linija);
                String naziv = input.readLine();
                Integer brojStanovnika = Integer.parseInt(input.readLine());
                Integer brojZarazenihStanovnika = Integer.parseInt(input.readLine());
                Zupanija tmpZupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenihStanovnika);
                zupanije.add(tmpZupanija);
            }
        }catch (IOException ex){
            System.err.println("Pogreška kod čitanja iz datoteke: "+FILEZUPANIJE);
            ex.printStackTrace();
        }

        return zupanije;
    }

    /**
     * Služi za unos simptoma, te tako unešen simptom vraća kao privremeni objekt simptoma u main metodu gdje ga sprema u polje simptoma.
     * @return Vraća privremeni objekt klase Simptom.
     */
    private static Set<Simptom> unosSimptoma(Set<Simptom> simptomi) {
        System.out.println("Učitavanje podataka o simptomima...");
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILESIMPTOMI), Charset.forName("UTF-8")))){
            String linija = null;
            while((linija = input.readLine()) != null){
                Long id = Long.parseLong(linija);
                String naziv = input.readLine();
                String vrijednostSimptoma = input.readLine();
                VrijednostSimptoma vrijednostSimptomaEnum = null;
                switch (vrijednostSimptoma){
                    case "RIJETKO": vrijednostSimptomaEnum = VrijednostSimptoma.RIJETKO; break;
                    case "SREDNJE": vrijednostSimptomaEnum = VrijednostSimptoma.SREDNJE; break;
                    case "ČESTO": vrijednostSimptomaEnum = VrijednostSimptoma.CESTO; break;
                }
                Simptom tmpSimptom = new Simptom(id, naziv, vrijednostSimptomaEnum);
                simptomi.add(tmpSimptom);
            }

        }catch (IOException ex){
            System.err.println("Pogreška kod čitanja iz datoteke: "+FILESIMPTOMI);
            ex.printStackTrace();
        }

        return simptomi;
    }

    /**
     * Služi za unos bolesti, te tako unešenu bolest vraća kao privremeni objekt bolesti u main metodu gdje ju sprema u polje bolesti.
     * @param bolesti Polje objekata klase Bolest koje služi za provjeru je li ista bolest prethodno unešena.
     * @param simptomi Plje objekata klase Simptom koji služe za provjeru je li su isti simptomi prethodno unešeni.
     * @return Vraća privremeni objekt klase Bolest.
     */
    private static Set<Bolest> unosBolesti(Set <Bolest> bolesti, Set<Simptom> simptomi) {
        System.out.println("Učitavanje podataka o bolestima...");
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILEBOLESTI), Charset.forName("UTF-8")))){
            String linija = null;
            while((linija = input.readLine()) != null){
                Long id = Long.parseLong(linija);
                String naziv = input.readLine();
                String idSimptoma = input.readLine();
                String poljeIdSimptoma[] = idSimptoma.split(",");
                List<Simptom> simptomiBolesti = new ArrayList<>();
                for(Simptom simptom: simptomi){
                    for(int i = 0; i < poljeIdSimptoma.length; i++){
                        Long tmpIdSimptoma = Long.parseLong(poljeIdSimptoma[i]);
                        if(simptom.getId() == tmpIdSimptoma){
                            simptomiBolesti.add(simptom);
                        }
                    }
                }
                Bolest tmpBolest = new Bolest(id, naziv, simptomiBolesti);
                bolesti.add(tmpBolest);

            }
        }catch (IOException ex){
            System.err.println("Pogreška kod čitanja iz datoteke: "+FILEBOLESTI);
            ex.printStackTrace();
        }
        return bolesti;
    }

    private static Set<Bolest> unosVirusa(Set <Bolest> bolesti, Set<Simptom> simptomi) {
        System.out.println("Učitavanje podataka o virusima...");
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILEVIRUSI), Charset.forName("UTF-8")))){
            String linija = null;
            while((linija = input.readLine()) != null){
                Long id = Long.parseLong(linija);
                String naziv = input.readLine();
                String idSimptoma = input.readLine();
                String poljeIdSimptoma[] = idSimptoma.split(",");
                List<Simptom> simptomiBolesti = new ArrayList<>();
                for(Simptom simptom: simptomi){
                    for(int i = 0; i < poljeIdSimptoma.length; i++){
                        Long tmpIdSimptoma = Long.parseLong(poljeIdSimptoma[i]);
                        if(simptom.getId() == tmpIdSimptoma){
                            simptomiBolesti.add(simptom);
                        }
                    }
                }
                Virus tmpVirus = new Virus(id, naziv, simptomiBolesti);
                bolesti.add(tmpVirus);

            }
        }catch (IOException ex){
            System.err.println("Pogreška kod čitanja iz datoteke: "+FILEVIRUSI);
            ex.printStackTrace();
        }
        return bolesti;
    }

    /**
     * Služi za unos osoba, te tako unešenu osobu vraća kao privremeni objekt osoba u main metodu gdje ju sprema u polje osoba.
     * @param zupanije Polje objekata klase Županija koje služi za ispis korisniku koje sve županije može odabrati za osobu koju unosi.
     * @param bolesti Polje objekata klase Bolesti koje služi za ispis korisniku koje sve bolesti može odabrati za osobu koju unosi.
     * @param osobe Polje objekata klase Osoba koje služi za ispis korisniku koje sve osobe s kojima je osoba koju unosi bila u kontaktu.
     * @return Vraća privremeni objekt klase Osoba.
     */
    private static List<Osoba> unosOsoba(List<Zupanija> zupanije, Set <Bolest> bolesti, List<Osoba> osobe) {
        System.out.println("Učitavanje osoba...");
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILEOSOBE), Charset.forName("UTF-8")))){
            String linija = null;
            Boolean prvaOsoba = true;
            while((linija = input.readLine()) != null){
                Long idOsobe = Long.parseLong(linija);
                String imeOsobe = input.readLine();
                String prezimeOsobe = input.readLine();
                Long idZupanije = Long.parseLong(input.readLine());
                Zupanija zupanijaOsobe = null;
                for(Zupanija zupanija:zupanije){
                    if(zupanija.getId() == idZupanije){
                        zupanijaOsobe = zupanija;
                    }
                }
                Long idBolesti = Long.parseLong(input.readLine());
                Bolest bolestOsobe = null;
                for(Bolest bolest: bolesti){
                    if(bolest.getId() == idBolesti){
                        bolestOsobe = bolest;
                    }
                }

                if(prvaOsoba == false){
                    String idKontaktiraneOsobe = input.readLine();
                    String poljeIdKontaktiraneOsobe[] = idKontaktiraneOsobe.split(",");
                    List<Osoba> kontaktiraneOsobe = new ArrayList<>();
                    for(int i = 0; i < poljeIdKontaktiraneOsobe.length; i++){
                        Long tmpIdKontaktiraneOsobe = Long.parseLong(poljeIdKontaktiraneOsobe[i]);
                        kontaktiraneOsobe.add(osobe.get((int) (tmpIdKontaktiraneOsobe-1)));
                    }
                    Osoba tmpOsoba = new Osoba.Builder(idOsobe)
                            .ime(imeOsobe)
                            .prezime(prezimeOsobe)
                            .zupanija(zupanijaOsobe)
                            .bolest(bolestOsobe)
                            .kontaktiraneOsobe(kontaktiraneOsobe)
                            .build();

                    osobe.add(tmpOsoba);
                }else{
                    Osoba tmpOsoba = new Osoba.Builder(idOsobe)
                            .ime(imeOsobe)
                            .prezime(prezimeOsobe)
                            .zupanija(zupanijaOsobe)
                            .bolest(bolestOsobe)
                            .build();

                    osobe.add(tmpOsoba);
                }
                prvaOsoba = false;

            }
        }catch (IOException ex){
            System.err.println("Pogreška kod čitanja iz datoteke: "+FILEOSOBE);
            ex.printStackTrace();
        }

        return osobe;
    }


    /**
     * Ispisuje korisniku ime, prezime, starost, županiju prebivališta osobe koju je unio,
     * te njihovu bolest kao i osobe s kojima je ta osoba bila u kontaktu.
     * @param osobe Polje objekata klase Osoba koje služi za ispis svih osoba koje su unesene.
     */
    private static void popisOsoba(List<Osoba> osobe){
        System.out.println("Popis osoba:");
        for(int i = 0; i < osobe.size(); i++){
            System.out.println("Ime i prezime: "+ osobe.get(i).getIme()+" "+ osobe.get(i).getPrezime());
            System.out.println("Starost: "+ osobe.get(i).getDatumRodjenja());
            System.out.println("Županija prebivališta: "+ osobe.get(i).getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: "+ osobe.get(i).getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if(osobe.get(i).getKontaktiraneOsobe().size() > 0){
                for(int j = 0; j < osobe.get(i).getKontaktiraneOsobe().size(); j++){
                    System.out.println(osobe.get(i).getKontaktiraneOsobe().get(j).getIme()+" "+ osobe.get(i).getKontaktiraneOsobe().get(j).getPrezime());
                }
            }else{
                System.out.println("Nema kontaktiranih osoba.");
            }
        }
    }



    private static void mapirajOboljeleOsobe(Map<Bolest, List<Osoba>> mapaOboljelihOsoba, List<Osoba> osobe){

        for(Osoba osoba:osobe){
            if(mapaOboljelihOsoba.containsKey(osoba.getZarazenBolescu())){
                mapaOboljelihOsoba.get(osoba.getZarazenBolescu()).add(osoba);

            }else{
                List <Osoba> listaOboljelih = new ArrayList<>();
                mapaOboljelihOsoba.put(osoba.getZarazenBolescu(), listaOboljelih);
                mapaOboljelihOsoba.get(osoba.getZarazenBolescu()).add(osoba);
            }

        }
    }

    private static void ispisiOboljeleOsobe(Map<Bolest, List<Osoba>> mapaOboljelihOsoba, Set<Bolest> bolesti){
        List <Osoba> osobeBolest = null;
        List <Osoba> osobeVirus = null;

        for(Bolest bolest: bolesti){
            if(mapaOboljelihOsoba.containsKey(bolest)){
                if(bolest instanceof Virus){
                    osobeVirus = mapaOboljelihOsoba.get(bolest);
                }else{
                    osobeBolest = mapaOboljelihOsoba.get(bolest);
                }

                if(bolest instanceof Virus) {
                    System.out.print("Od virusa " + bolest.getNaziv() + " boluju: ");
                    for (int i = 0; i < osobeVirus.size(); i++) {
                        System.out.print(osobeVirus.get(i).getIme() + " " + osobeVirus.get(i).getPrezime() + " ");
                    }
                    System.out.println("");
                }else{
                    System.out.print("Od bolesti "+bolest.getNaziv() + " boluju: ");
                    for (int i = 0; i < osobeBolest.size(); i++) {
                        System.out.print(osobeBolest.get(i).getIme() + " " + osobeBolest.get(i).getPrezime() + " ");
                    }
                    System.out.println("");
                }
            }
        }
    }

    private static void ispisZupanijeSNajvecimPostotkomOboljelih(List<Zupanija> zupanije){
        SortedSet<Zupanija> sortiraneZupanije = new TreeSet<>(new CovidSorter());
        for(Zupanija zupanija:zupanije){
            sortiraneZupanije.add(zupanija);
        }

        System.out.println("Najviše zaraženih osoba ima u županiji "+sortiraneZupanije.first().getNaziv()+": "+sortiraneZupanije.first().getPostotakZarazenosti()+"%");
    }

    private static KlinikaZaInfektivneBolesti napuniKlinikuZaInfektivneBolesti(Set<Bolest> bolesti, List<Osoba> osobe){

        List<Virus> virusiZaKliniku = new ArrayList<>();
        List<Osoba> osobeZaKliniku = new ArrayList<>();

        for(Bolest virus: bolesti){
            if(virus instanceof Virus) {
                virusiZaKliniku.add((Virus) virus);
            }
            for(Osoba osoba: osobe){
                if(osoba.getZarazenBolescu().equals(virus)){
                    osobeZaKliniku.add(osoba);
                }
            }
        }

        return new KlinikaZaInfektivneBolesti<Virus, Osoba>(virusiZaKliniku, osobeZaKliniku);
    }

    private static void sortirajVirusePomocuLambde(Set<Bolest> bolesti, KlinikaZaInfektivneBolesti klinikaZaInfektivneBolesti){
        List<Bolest> virusi = bolesti.stream()
                .filter(bolest -> bolest instanceof Virus)
                .collect(Collectors.toList());

        virusi = virusi.stream().sorted(Comparator.comparing(Bolest::getNaziv).reversed())
                .collect(Collectors.toList());

        Object virusiIzKlinike = klinikaZaInfektivneBolesti.getVirusi().stream()
                .sorted(Comparator.comparing(Virus::getNaziv).reversed())
                .collect(Collectors.toList());

        klinikaZaInfektivneBolesti.setVirusi((List) virusiIzKlinike);

    }

    private static void sortirajViruseBezLambde(Set <Bolest> bolesti, KlinikaZaInfektivneBolesti klinikaZaInfektivneBolesti){
        List<Bolest> virusi = new ArrayList<>();
        List<Bolest> virusiIzKlinike = new ArrayList<>();

        for(Bolest bolest: bolesti){
            if(bolest instanceof Virus){
                virusi.add(bolest);
            }
        }
        Collections.sort(virusi, new Comparator<Bolest>() {
            @Override
            public int compare(Bolest o1, Bolest o2) {
                return o1.getNaziv().compareTo(o2.getNaziv());
            }
        });

        List<Bolest> tmpVirusi = new ArrayList<>();
        for(int i = virusi.size()-1; i >= 0; i--){
            tmpVirusi.add(virusi.get(i));
        }

        virusiIzKlinike = klinikaZaInfektivneBolesti.getVirusi();

        virusiIzKlinike.sort(new Comparator<Bolest>() {
            @Override
            public int compare(Bolest o1, Bolest o2) {
                return o1.getNaziv().compareTo(o2.getNaziv());
            }
        });

        List<Bolest> tmpVirusiIzKlinike = new ArrayList<>();
        for(int i = virusiIzKlinike.size()-1; i >= 0; i--){
            tmpVirusiIzKlinike.add(virusiIzKlinike.get(i));
        }
    }

    private static void filtrirajPoStringu(Scanner unos, List<Osoba> osobe){
        System.out.print("Unesite string za pretragu po prezimenu: ");
        String stringZaPretragu = unos.nextLine();

        List<Osoba> filtriraneOsobe = Optional.of(osobe.stream()
                .filter(osoba -> osoba.getPrezime().contains(stringZaPretragu))
                .collect(Collectors.toList()))
                .stream()
                .findAny()
                .orElse(null);


        if(!filtriraneOsobe.isEmpty()){
            System.out.println("Osobe čije prezime sadrži "+stringZaPretragu+" su sljedeće:");
            filtriraneOsobe.stream()
                    .forEach(System.out::println);
        }else{
            System.out.println("Niti jedna osoba ne sadrži "+stringZaPretragu+" u prezimenu.");
        }

    }

    private static void mapirajSimptomeBolesti(Set<Bolest> bolesti){
        List<Bolest> listaBolesti = new ArrayList<>();

        for(Bolest bolest: bolesti){
            listaBolesti.add(bolest);
        }

        List<Integer> brojSimptoma = listaBolesti.stream()
                .map(bolest -> bolest.getSimptomi().size())
                .collect(Collectors.toList());

        for(int i = 0; i < listaBolesti.size(); i++){
            System.out.println(listaBolesti.get(i).getNaziv()+" ima "+brojSimptoma.get(i)+" simptoma.");
        }

    }

    private static void serializirajZupanije(List<Zupanija> zupanije){
        List<Zupanija> zupanijeZaSerijalizaciju = new ArrayList<>();

        for(Zupanija zupanija:zupanije){
            if(zupanija.getPostotakZarazenosti() > 2){
                zupanijeZaSerijalizaciju.add(zupanija);
            }
        }

        if(!zupanijeZaSerijalizaciju.isEmpty()){
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILESERIJALIZACIJA))){
                out.writeObject(zupanijeZaSerijalizaciju);

            }catch (IOException ex){
                System.err.println("Serijalizacija neuspješna");
                ex.printStackTrace();
            }
        }
    }

}


