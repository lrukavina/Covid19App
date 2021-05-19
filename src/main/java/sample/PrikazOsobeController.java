package main.java.sample;

import hr.java.covidportal.model.Osoba;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class PrikazOsobeController {

    @FXML
    private Label imePrezimeOsobe;

    @FXML
    private Label starostOsobe;

    @FXML
    private Label zupanijaOsobe;

    @FXML
    private Label bolestosobe;

    @FXML
    private Label kontaktiOsobe;



    public void prikaziOsobu(Osoba osoba){
        this.imePrezimeOsobe.setText(osoba.getIme() +" "+ osoba.getPrezime());
        this.starostOsobe.setText(osoba.getDatumRodjenja().toString());
        this.zupanijaOsobe.setText(osoba.getZupanija().getNaziv());
        this.bolestosobe.setText(osoba.getZarazenBolescu().getNaziv());
        this.kontaktiOsobe.setText(osoba.getKontaktiraneOsobe().toString());

    }
}
