package hr.java.covidportal.iznimke;

/**
 * Služi za bacanje iznimke u slučaju ako je korisnik odabrao kontaktiranu osobu koju je već prethodno odabrao.
 */
public class DuplikatKontaktiraneOsobe extends Exception {

    /**
     * Ispisuje korisniku poruku o pogrešci u konzolu.
     * @param message Poruka koju ispsiuje korisniku u konzolu.
     */
    public DuplikatKontaktiraneOsobe(String message){
        super(message);
    }

    /**
     * Baca uzrok zbog kojeg je došlo do iznimke.
     * @param cause Uzrok zbog kojeg je došlo do iznimke.
     */
    public DuplikatKontaktiraneOsobe(Throwable cause){
        super(cause);
    }

    /**
     * Služi za istovremeno bacanje uzroka i ispisivanje poruke o pogrešci korisniku u konzolu.
     * @param message Poruka koju ispsiuje korisniku u konzolu.
     * @param cause Uzrok zbog kojeg je došlo do iznimke.
     */
    public DuplikatKontaktiraneOsobe(String message, Throwable cause){
        super(message, cause);
    }
}
