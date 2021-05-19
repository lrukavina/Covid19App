package main.java.sample;

import hr.java.covidportal.baza.BazaPodataka;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Zupanija;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class DodavanjeNoveZupanijeController implements Initializable {

    static final String FILEZUPANIJE = "dat/zupanije.txt";
    Path datotekaZaPisanje = Path.of(FILEZUPANIJE);
    ObservableValue<Integer> idZupanije;


    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField brojStanovnikaZupanije;

    @FXML
    private TextField brojZarazenihStanovnikaZupanije;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Long dohvatiIdZupanije() throws IOException, SQLException {
        List<Zupanija> zupanije = BazaPodataka.dohvatiSveZupanijeIzBaze();

        Zupanija zadnjaZupanija = zupanije.get(zupanije.size() - 1);

        return zadnjaZupanija.getId()+1;
    }

    @FXML
    public void spremiZupaniju(){

        try{
            Long idZupanijeWrite = dohvatiIdZupanije();
            String nazivZupanijeWrite = nazivZupanije.getText();
            Integer brojStanovnikaZupanijeWrite = Integer.parseInt(brojStanovnikaZupanije.getText());
            Integer brojZarazenihStanovnikaZupanijeWrite = Integer.parseInt(brojZarazenihStanovnikaZupanije.getText());

            Zupanija novaZupanija = new Zupanija(idZupanijeWrite, nazivZupanijeWrite, brojStanovnikaZupanijeWrite, brojZarazenihStanovnikaZupanijeWrite);

            BazaPodataka.spremiNovuZupaniju(novaZupanija);

            Alert dodanaZupanija = new Alert(Alert.AlertType.INFORMATION);
            dodanaZupanija.setTitle("Dodavanje nove županije");
            dodanaZupanija.setHeaderText("Uspješno dodana nova županija");
            dodanaZupanija.setContentText("Županija "+nazivZupanijeWrite+" uspješno spremljena u datoteku.");
            dodanaZupanija.showAndWait();


        }catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
