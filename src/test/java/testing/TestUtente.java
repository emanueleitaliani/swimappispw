package testing;

import Bean.LezioneBean;
import Bean.Prenotazionebean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.LezioniNonTrovateException;
import Other.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtente {

    private Prenotazionecontroller controller;

    @BeforeEach
    void setUp() {
        Config.setPersistenceType("mysql");
        controller = new Prenotazionecontroller();
    }

    @Test
    void testRicercaLezioneFiltriInesistenti() {
        LezioneBean filtri = new LezioneBean();
        filtri.setTipoLezione("SportInesistente");

        assertThrows(LezioniNonTrovateException.class, () -> {
            controller.ricercaLezione(filtri);
        }, "Dovrebbe lanciare eccezione se non trova lezioni");
    }

    @Test
    void testRichiediPrenotazioneDuplicata() {
        Prenotazionebean p = new Prenotazionebean();
        p.setEmailUser("Leoboria11@gmail.com");
        p.setEmailIstruttore("istruttore@test.it");
        p.setGiorno("Lunedì");
        p.setHour(10.0f); // Float come da specifica del Bean

        // Il test passa se, tentando di prenotare due volte lo stesso slot,
        // il controller blocca la seconda richiesta.
        assertThrows(LezioneGiaPrenotataException.class, () -> {
            controller.richiediprenotazione(p);
            controller.richiediprenotazione(p);
        });
    }

    @Test
    void testGetPrenotazioniByEmail() {
        assertDoesNotThrow(() -> {
            List<Prenotazionebean> lista = controller.getPrenotazioniByEmail("Leoboria11@gmail.com");
            assertNotNull(lista);
        });
    }
}