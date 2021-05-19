package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Virus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.*;


public class DodavanjeNovogVirusaController implements Initializable {

    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEVIRUSI = "dat/virusi.txt";
    Path datotekaZaPisanje = Path.of(FILEVIRUSI);
    ObservableValue<Integer> idVirusa;
    Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptomeIzBaze();


    @FXML
    private TextField nazivVirusa;

    @FXML
    ListView<Simptom> simptomListView = new ListView<>();

    public DodavanjeNovogVirusaController() throws IOException, SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        simptomListView.getItems().addAll(simptomi);
        simptomListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public Long dohvatiIdVirusa() throws IOException, SQLException {
        Set<Bolest> bolesti = BazaPodataka.dohvatiSveBolestiIzBaze();

        Long id = Long.parseLong("0");

        for(Bolest bolest:bolesti){
            if(bolest.getId() > id){
                id = bolest.getId();
            }
        }

        return id+1;

    }

    public List<Simptom>  dohvatiSimptomeVirusa(ListView<Simptom> simptomListView, Set<Simptom> simptomi){
        ObservableList<Simptom> odabraniSimptomi = simptomListView.getSelectionModel().getSelectedItems();
        List<Simptom> odabraniSimptomiReturn = new ArrayList<>();

        odabraniSimptomiReturn.addAll(odabraniSimptomi);

        return odabraniSimptomiReturn;

    }

    @FXML
    public void spremiVirus(){

        try{
            Long idVirusaWrite = dohvatiIdVirusa();
            String nazivVirusaWrite = nazivVirusa.getText();
            List<Simptom>  simptomiVirusaWrite = dohvatiSimptomeVirusa(simptomListView, simptomi);

            Virus noviVirus = new Virus(idVirusaWrite, nazivVirusaWrite, simptomiVirusaWrite);

            BazaPodataka.spremiNovuBolest(noviVirus);

            Alert dodaniSimptom = new Alert(Alert.AlertType.INFORMATION);
            dodaniSimptom.setTitle("Dodavanje novog virusa");
            dodaniSimptom.setHeaderText("Uspješno dodan novi virus");
            dodaniSimptom.setContentText("Virus "+nazivVirusaWrite+" uspješno spremljen u datoteku.");
            dodaniSimptom.showAndWait();


        }catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
