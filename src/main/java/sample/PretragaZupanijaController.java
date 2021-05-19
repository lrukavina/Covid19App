package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.model.Zupanija;
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


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {
    static final String FILEZUPANIJE = "dat/zupanije.txt";
    private static ObservableList<Zupanija> observableListaZupanija;
    private static ObservableList<Zupanija> filtriranaObservableListaZupanija = FXCollections.observableArrayList();
    Boolean prvoPokretanje = true;

    @FXML
    List<Zupanija> zupanije = new ArrayList<>();

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnika;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenih;


    @FXML
    public void prikaziGlavniEkran() throws IOException {
        Parent glavniEkranFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Scene glavniEkranScena = new Scene(glavniEkranFrame, 700, 310);
        Main.getMainStage().setScene(glavniEkranScena);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        stupacBrojStanovnika.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        stupacBrojZarazenih.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenihOsoba"));

        if(observableListaZupanija == null) {
            observableListaZupanija = FXCollections.observableArrayList();
        }
        if(prvoPokretanje){
            observableListaZupanija.clear();
            try {
                zupanije = BazaPodataka.dohvatiSveZupanijeIzBaze();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            observableListaZupanija.addAll(zupanije);
            tablicaZupanija.setItems(observableListaZupanija);
            prvoPokretanje = false;
        }
    }

    public static ObservableList<Zupanija> getListaZupanija() {
        return observableListaZupanija;
    }

    public static void setListaZupanija(ObservableList<Zupanija> observableList) {
        observableListaZupanija = observableList;
    }

    @FXML
    public void pretraziZupanije(){
        String naziv = nazivZupanije.getText();

        List<Zupanija> filtriranaListaZupanija = observableListaZupanija.stream()
                .filter(zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv))
                .collect(Collectors.toList());

        filtriranaObservableListaZupanija.clear();
        filtriranaObservableListaZupanija.addAll(FXCollections.observableArrayList(filtriranaListaZupanija));
        tablicaZupanija.setItems(filtriranaObservableListaZupanija);
    }
}
