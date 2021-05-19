package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Virus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable{
    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEBOLESTI = "dat/bolesti.txt";
    private static ObservableList<Bolest> observableListaBolesti;
    private static ObservableList<Bolest> filtriranaObservableListaBolesti = FXCollections.observableArrayList();
    Boolean prvoPokretanje = true;

    Set<Simptom> simptomi = new HashSet<>();

    @FXML
    Set<Bolest> bolesti = new HashSet<>();

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;

    @FXML
    private TableColumn<Bolest, Set<Simptom>> stupacSetSimptoma;


    @FXML
    public void prikaziGlavniEkran() throws IOException {
        Parent glavniEkranFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Scene glavniEkranScena = new Scene(glavniEkranFrame, 700, 310);
        Main.getMainStage().setScene(glavniEkranScena);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        stupacSetSimptoma.setCellValueFactory(new PropertyValueFactory<Bolest, Set<Simptom>>("simptomi"));

        if(observableListaBolesti == null) {
            observableListaBolesti = FXCollections.observableArrayList();
        }
        if(prvoPokretanje){
            observableListaBolesti.clear();

            try {
                bolesti = BazaPodataka.dohvatiSveBolestiIzBaze();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<Bolest> bolestiZaPrikaz = new HashSet<>();
            for(Bolest bolest:bolesti){
                if(bolest instanceof Virus){

                }
                else{
                    bolestiZaPrikaz.add(bolest);
                }
            }
            observableListaBolesti.addAll(bolestiZaPrikaz);
            tablicaBolesti.setItems(observableListaBolesti);
            prvoPokretanje = false;
        }
    }

    public static ObservableList<Bolest> getListaBolesti() {
        return observableListaBolesti;
    }

    public static void setListaBolesti(ObservableList<Bolest> observableList) {
        observableListaBolesti = observableList;
    }

    @FXML
    public void pretraziBolesti(){
        String naziv = nazivBolesti.getText();

        List<Bolest> filtriranaListaBolesti = observableListaBolesti.stream()
                .filter(bolest -> bolest.getNaziv().toLowerCase().contains(naziv))
                .collect(Collectors.toList());

        filtriranaObservableListaBolesti.clear();
        filtriranaObservableListaBolesti.addAll(FXCollections.observableArrayList(filtriranaListaBolesti));
        tablicaBolesti.setItems(filtriranaObservableListaBolesti);
    }
}
