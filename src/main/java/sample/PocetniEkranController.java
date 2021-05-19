package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PocetniEkranController implements Initializable {
    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    @FXML
    public void dodavanjeNoveZupanije() throws IOException{
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveZupanije.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    @FXML
    public void dodavanjeNovogSimptoma() throws IOException{
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 894, 400);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    @FXML
    public void dodavanjeNovebolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 894, 400);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    @FXML
    public void dodavanjeNovogVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 894, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    @FXML
    public void dodavanjeNoveOsobe() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveOsobe.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 894, 583);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
