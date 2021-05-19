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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {
    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEVIRUSI = "dat/virusi.txt";
    private static ObservableList<Virus> observableListaVirusa;
    private static ObservableList<Virus> filtriranaObservableListaVirusa = FXCollections.observableArrayList();
    Boolean prvoPokretanje = true;

    Set<Bolest> bolesti = new HashSet<>();

    @FXML
    Set<Virus> virusi = new HashSet<>();

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Virus> tablicaVirusa;

    @FXML
    private TableColumn<Virus, String> stupacNazivVirusa;

    @FXML
    private TableColumn<Virus, Set<Simptom>> stupacSetSimptoma;

    @FXML
    public void prikaziGlavniEkran() throws IOException {
        Parent glavniEkranFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Scene glavniEkranScena = new Scene(glavniEkranFrame, 700, 310);
        Main.getMainStage().setScene(glavniEkranScena);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));
        stupacSetSimptoma.setCellValueFactory(new PropertyValueFactory<Virus, Set<Simptom>>("simptomi"));

        if(observableListaVirusa == null) {
            observableListaVirusa = FXCollections.observableArrayList();
        }
        if(prvoPokretanje){
            observableListaVirusa.clear();

            try {
                bolesti = BazaPodataka.dohvatiSveBolestiIzBaze();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(Bolest bolest:bolesti){
                if(bolest instanceof Virus){
                    virusi.add((Virus) bolest);
                }
            }

            observableListaVirusa.addAll(virusi);
            tablicaVirusa.setItems(observableListaVirusa);
            prvoPokretanje = false;
        }
    }

    public static ObservableList<Virus> getListaVirusa() {
        return observableListaVirusa;
    }

    public static void setListaVirusa(ObservableList<Virus> observableList) {
        observableListaVirusa = observableList;
    }

    @FXML
    public void pretraziViruse(){
        String naziv = nazivVirusa.getText();

        List<Virus> filtriranaListaVirusa = observableListaVirusa.stream()
                .filter(virus -> virus.getNaziv().toLowerCase().contains(naziv))
                .collect(Collectors.toList());

        filtriranaObservableListaVirusa.clear();
        filtriranaObservableListaVirusa.addAll(FXCollections.observableArrayList(filtriranaListaVirusa));
        tablicaVirusa.setItems(filtriranaObservableListaVirusa);
    }
}
