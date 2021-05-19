package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {
    static final String FILEZUPANIJE = "dat/zupanije.txt";
    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEBOLESTI = "dat/bolesti.txt";
    static final String FILEVIRUSI = "dat/virusi.txt";
    static final String FILEOSOBE = "dat/osobe.txt";
    private static ObservableList<Osoba> observableListaOsoba;
    private static ObservableList<Osoba> filtriranaObservableListaOsoba = FXCollections.observableArrayList();
    Boolean prvoPokretanje = true;

    List<Zupanija> zupanije = new ArrayList<>();
    Set<Simptom> simptomi = new HashSet<>();
    Set<Bolest> bolesti = new HashSet<>();

    @FXML
    List<Osoba> osobe = new ArrayList<>();

    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, String> stupacImeOsobe;

    @FXML
    private TableColumn<Osoba, String> stupacPrezimeOsobe;

    @FXML
    private TableColumn<Osoba, Integer> stupacStarostOsobe;

    @FXML
    private TableColumn<Osoba, Zupanija> stupacZupanijaOsobe;

    @FXML
    private TableColumn<Osoba, Bolest> stupacBolestiOsobe;

    @FXML
    private TableColumn<Osoba, List<Osoba>> stupacKontaktOsoba;




    @FXML
    public void prikaziGlavniEkran() throws IOException {
        Parent glavniEkranFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Scene glavniEkranScena = new Scene(glavniEkranFrame, 700, 310);
        Main.getMainStage().setScene(glavniEkranScena);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        stupacStarostOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        stupacZupanijaOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));
        stupacBolestiOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));
        stupacKontaktOsoba.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

        if(observableListaOsoba == null) {
            observableListaOsoba = FXCollections.observableArrayList();
        }
        if(prvoPokretanje){
            observableListaOsoba.clear();
            try {
                osobe = BazaPodataka.dohvatiSveOsobeIzBaze();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            observableListaOsoba.addAll(osobe);
            tablicaOsoba.setItems(observableListaOsoba);
            prvoPokretanje = false;
        }

        tablicaOsoba.setRowFactory(tablicaOsoba -> {
            TableRow<Osoba> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Osoba osoba = row.getItem();
                    kliknuto(osoba);
                }
            });
            return row;
        });
    }

    public static ObservableList<Osoba> getListaOsoba() {
        return observableListaOsoba;
    }

    public static void setListaOsoba(ObservableList<Osoba> observableList) {
        observableListaOsoba = observableList;
    }

    @FXML
    public void pretraziOsobe(){
        String ime = imeOsobe.getText();
        String prezime = prezimeOsobe.getText();

        List<Osoba> filtriranaListaOsoba = observableListaOsoba.stream()
                .filter(osoba -> osoba.getIme().toLowerCase().contains(ime))
                .filter(osoba -> osoba.getPrezime().toLowerCase().contains(prezime))
                .collect(Collectors.toList());

        filtriranaObservableListaOsoba.clear();
        filtriranaObservableListaOsoba.addAll(FXCollections.observableArrayList(filtriranaListaOsoba));
        tablicaOsoba.setItems(filtriranaObservableListaOsoba);
    }

    public void kliknuto(Osoba osoba){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PrikazOsobe.fxml"));

            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Prikaz osobe");
            stage.setScene(scene);
            stage.show();

            PrikazOsobeController controller = loader.getController();
            controller.prikaziOsobu(osoba);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
