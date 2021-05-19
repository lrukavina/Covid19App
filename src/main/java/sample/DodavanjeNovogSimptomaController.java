package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.model.Simptom;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


public class DodavanjeNovogSimptomaController implements Initializable {

    static final String FILESIMPTOMI = "dat/simptomi.txt";
    Path datotekaZaPisanje = Path.of(FILESIMPTOMI);
    ObservableValue<Integer> idSimptoma;


    @FXML
    private TextField nazivSimptoma;

    @FXML
    ChoiceBox<String> vrijednostSimptoma = new ChoiceBox<>();

    ComboBox<String> comboBox = new ComboBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vrijednostSimptoma.getItems().addAll("RIJETKO","SREDNJE","ČESTO","Produktivni","Intenzivno","Visoka","Jaka");
        vrijednostSimptoma.setValue("RIJETKO");

    }

    public Long dohvatiIdSimptoma() throws IOException, SQLException {
        Set<Simptom> simptomi = BazaPodataka.dohvatiSveSimptomeIzBaze();
        Long id = Long.parseLong("0");

        for(Simptom simptom:simptomi){
            if(simptom.getId() > id){
                id = simptom.getId();
            }
        }

        return id+1;
    }

    public VrijednostSimptoma dohvatiVrijednostSimptoma(ChoiceBox<String> vrijednostSimptoma){
        String vrijednostSimptomaString = vrijednostSimptoma.getValue();

        VrijednostSimptoma vrijednostSimptomaEnum = null;
        switch (vrijednostSimptomaString) {
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

        return vrijednostSimptomaEnum;
    }

    @FXML
    public void spremiSimptom(){

        try{
            Long idSimptomaWrite = dohvatiIdSimptoma();
            String nazivSimptomaWrite = nazivSimptoma.getText();
            VrijednostSimptoma vrijednostSimptomaWrite = dohvatiVrijednostSimptoma(vrijednostSimptoma);

            Simptom noviSimptom = new Simptom(idSimptomaWrite, nazivSimptomaWrite, vrijednostSimptomaWrite);

            BazaPodataka.spremiNoviSimptom(noviSimptom);

            Alert dodaniSimptom = new Alert(Alert.AlertType.INFORMATION);
            dodaniSimptom.setTitle("Dodavanje novog simptoma");
            dodaniSimptom.setHeaderText("Uspješno dodan novi simptom");
            dodaniSimptom.setContentText("Simptom "+nazivSimptomaWrite+" uspješno spremljen u datoteku.");
            dodaniSimptom.showAndWait();
            


        }catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
