package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.*;


public class DodavanjeNoveBolestiController implements Initializable {

    static final String FILESIMPTOMI = "dat/simptomi.txt";
    static final String FILEBOLESTI = "dat/bolesti.txt";
    Path datotekaZaPisanje = Path.of(FILEBOLESTI);
    Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptomeIzBaze();


    @FXML
    private TextField nazivBolesti;

    @FXML
    ListView<Simptom> simptomListView = new ListView<>();

    public DodavanjeNoveBolestiController() throws IOException, SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        simptomListView.getItems().addAll(simptomi);
        simptomListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public Long dohvatiIdBolesti() throws IOException, SQLException {
        Set<Bolest> bolesti = BazaPodataka.dohvatiSveBolestiIzBaze();

        Long id = Long.parseLong("0");

        for(Bolest bolest:bolesti){
            if(bolest.getId() > id){
                id = bolest.getId();
            }
        }

        return id+1;

    }

    public List<Simptom>  dohvatiSimptomeBolesti(ListView<Simptom> simptomListView, Set<Simptom> simptomi){
        ObservableList<Simptom> odabraniSimptomi = simptomListView.getSelectionModel().getSelectedItems();
        List<Simptom> odabraniSimptomiReturn = new ArrayList<>();

        odabraniSimptomiReturn.addAll(odabraniSimptomi);

        return odabraniSimptomiReturn;

    }



    @FXML
    public void spremiBolest(){

        try{
            Long idBolestiWrite = dohvatiIdBolesti();
            String nazivBolestiWrite = nazivBolesti.getText();
            List<Simptom>  simptomiBolestiWrite = dohvatiSimptomeBolesti(simptomListView, simptomi);

            Bolest novaBolest = new Bolest(idBolestiWrite, nazivBolestiWrite, simptomiBolestiWrite);

            BazaPodataka.spremiNovuBolest(novaBolest);

            Alert dodaniSimptom = new Alert(Alert.AlertType.INFORMATION);
            dodaniSimptom.setTitle("Dodavanje nove bolesti");
            dodaniSimptom.setHeaderText("Uspješno dodana nova bolest");
            dodaniSimptom.setContentText("Bolest "+nazivBolestiWrite+" uspješno spremljena u datoteku.");
            dodaniSimptom.showAndWait();


        }catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
