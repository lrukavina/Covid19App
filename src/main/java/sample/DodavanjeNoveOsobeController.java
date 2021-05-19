package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DodavanjeNoveOsobeController implements Initializable {

    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEVIRUSI = "dat/virusi.txt";
    static final String FILEBOLESTI = "dat/bolesti.txt";
    static final String FILEZUPANIJE = "dat/zupanije.txt";
    static final String FILEOSOBE = "dat/osobe.txt";
    Path datotekaZaPisanje = Path.of(FILEOSOBE);
    List<Zupanija> zupanije = new ArrayList<>();
    Set<Simptom> simptomi = new HashSet<>();
    Set<Bolest> bolesti = new HashSet<>();
    List<Osoba> osobe = new ArrayList<>();


    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private DatePicker starostOsobe;

    @FXML
    ChoiceBox<String> zupanijaOsobe = new ChoiceBox<>();

    @FXML
    ChoiceBox<String> bolestOsobe = new ChoiceBox<>();

    @FXML
    ListView<Osoba> osobaListView = new ListView<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            zupanije.addAll(BazaPodataka.dohvatiSveZupanijeIzBaze());
            simptomi.addAll(BazaPodataka.dohvatiSveSimptomeIzBaze());
            bolesti.addAll(BazaPodataka.dohvatiSveBolestiIzBaze());
            osobe.addAll(BazaPodataka.dohvatiSveOsobeIzBaze());

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        for(Zupanija zupanija:zupanije){
            zupanijaOsobe.getItems().add(zupanija.getNaziv());
        }
        zupanijaOsobe.setValue("Odaberite županiju");

        for(Bolest bolest:bolesti){
            bolestOsobe.getItems().add(bolest.getNaziv());
        }
        bolestOsobe.setValue("Odaberite bolest");

        osobaListView.getItems().addAll(osobe);
        osobaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }


    public Long dohvatiIdOsobe(List<Osoba> osobe){
        Osoba zadnjaOsoba = osobe.get(osobe.size() - 1);

        return zadnjaOsoba.getId()+1;
    }

    public Zupanija dohvatiZupanijuOsobe(ChoiceBox<String> zupanijeOsobe, List<Zupanija> zupanije){
        String nazivZupanije = zupanijeOsobe.getValue();
        Zupanija novaZupanija = null;

        for(Zupanija zupanija: zupanije){
            if(nazivZupanije.equals(zupanija.getNaziv())){
                novaZupanija = zupanija;
            }
        }
        return novaZupanija;
    }

    public Bolest dohvatiBolestOsobe(ChoiceBox<String> bolestOsobe, Set<Bolest> bolesti){
        String nazivBolestiOsobe = bolestOsobe.getValue();
        Bolest novaBolest = null;

        for(Bolest bolest: bolesti){
            if(nazivBolestiOsobe.equals(bolest.getNaziv())){
                novaBolest = bolest;
            }
        }
        return novaBolest;
    }


    public List<Osoba> dohvatiKontaktOsobe(ListView<Osoba> osobaListView, List<Osoba> osobe){
        ObservableList<Osoba> odabraneKontaktOsobe = osobaListView.getSelectionModel().getSelectedItems();

        return odabraneKontaktOsobe;
    }

    @FXML
    public void spremiOsobu(){

        try{
            Long idOsobeWrite = dohvatiIdOsobe(osobe);
            String imeOsobeWrite = imeOsobe.getText();
            String prezimeOsobeWrite = prezimeOsobe.getText();
            LocalDate starostOsobeWrite = starostOsobe.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
            String starostOsobeWriteString = starostOsobeWrite.format(formatter);

            Date datumRodjenja= new SimpleDateFormat("yyyy-dd-MM").parse(starostOsobeWriteString);
            java.sql.Date datumRodjenjaBaza = new java.sql.Date(datumRodjenja.getTime());
            Zupanija zupanijaOsobeWrite = dohvatiZupanijuOsobe(zupanijaOsobe, zupanije);
            Bolest bolestOsobeWrite = dohvatiBolestOsobe(bolestOsobe, bolesti);
            List<Osoba> kontaktiraneOsobeWrite = dohvatiKontaktOsobe(osobaListView, osobe);

            Osoba novaOsoba = new Osoba.Builder(idOsobeWrite)
                    .ime(imeOsobeWrite)
                    .prezime(prezimeOsobeWrite)
                    .datumRodjenja(datumRodjenjaBaza)
                    .zupanija(zupanijaOsobeWrite)
                    .bolest(bolestOsobeWrite)
                    .kontaktiraneOsobe(kontaktiraneOsobeWrite)
                    .build();

            BazaPodataka.spremiNovuOsobu(novaOsoba);

            Alert dodaniSimptom = new Alert(Alert.AlertType.INFORMATION);
            dodaniSimptom.setTitle("Dodavanje nove osobe");
            dodaniSimptom.setHeaderText("Uspješno dodana nova osoba");
            dodaniSimptom.setContentText("Osoba "+imeOsobeWrite+" "+prezimeOsobeWrite+" uspješno je spremljena u bazu.");
            dodaniSimptom.showAndWait();


        }catch (IOException | ParseException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
