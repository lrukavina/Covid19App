package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.Simptom;
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
import java.util.*;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {
    static final String FILESIMPTOMI = "dat/simptomi.txt";
    private static ObservableList<Simptom> observableListaSimptoma;
    private static ObservableList<Simptom> filtriranaObservableListaSimptoma  = FXCollections.observableArrayList();
    Boolean prvoPokretanje = true;

    @FXML
    Set<Simptom> simptomi = new HashSet<>();

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;

    @FXML
    private TableColumn<Simptom, VrijednostSimptoma> stupacVrijednostSimptoma;


    @FXML
    public void prikaziGlavniEkran() throws IOException {
        Parent glavniEkranFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Scene glavniEkranScena = new Scene(glavniEkranFrame, 700, 310);
        Main.getMainStage().setScene(glavniEkranScena);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, VrijednostSimptoma>("vrijednost"));

        if(observableListaSimptoma == null) {
            observableListaSimptoma = FXCollections.observableArrayList();
        }
        if(prvoPokretanje){
            observableListaSimptoma.clear();

            try {
                simptomi = BazaPodataka.dohvatiSveSimptomeIzBaze();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            observableListaSimptoma.addAll(simptomi);
            tablicaSimptoma.setItems(observableListaSimptoma);
            prvoPokretanje = false;
        }
    }

    public static ObservableList<Simptom> getListaSimptoma() {
        return observableListaSimptoma;
    }

    public static void setListaSimptoma(ObservableList<Simptom> observableList) {
        observableListaSimptoma = observableList;
    }

    @FXML
    public void pretraziSimptome(){
        String naziv = nazivSimptoma.getText();

        List<Simptom> filtriranaListaSimptoma = observableListaSimptoma.stream()
                .filter(simptom -> simptom.getNaziv().toLowerCase().contains(naziv))
                .collect(Collectors.toList());

        filtriranaObservableListaSimptoma.clear();
        filtriranaObservableListaSimptoma.addAll(FXCollections.observableArrayList(filtriranaListaSimptoma));
        tablicaSimptoma.setItems(filtriranaObservableListaSimptoma);
    }
}
