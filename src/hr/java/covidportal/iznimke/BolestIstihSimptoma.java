package hr.java.covidportal.iznimke;

/**
 * Služi za bacanje iznimke tijekom izvođenja u slučaju da korisnik unese bolest istih simptoma kao već prethodno unešena bolest
 */
public class BolestIstihSimptoma extends RuntimeException{

    /**
     * Ispisuje korisniku poruku o pogrešci u konzolu.
     * @param message Poruka koju ispsiuje korisniku u konzolu.
     */
    public BolestIstihSimptoma(String message){
        super(message);
    }

    /**
     * Baca uzrok zbog kojeg je došlo do iznimke.
     * @param cause Uzrok zbog kojeg je došlo do iznimke.
     */
    public BolestIstihSimptoma(Throwable cause){
        super(cause);
    }

    /**
     * Služi za istovremeno bacanje uzroka i ispisivanje poruke o pogrešci korisniku u konzolu.
     * @param message Poruka koju ispsiuje korisniku u konzolu.
     * @param cause Uzrok zbog kojeg je došlo do iznimke.
     */
    public BolestIstihSimptoma(String message, Throwable cause){
        super(message, cause);
    }
}
