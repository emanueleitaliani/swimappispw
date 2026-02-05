package testing;

import Bean.LezioneBean;
import Bean.Prenotazionebean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.LezioniNonTrovateException;
import Exceptions.UtentenonpresenteException;
import Other.Config;
import Other.Stampa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
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
        // 1. Arrange
        LezioneBean filtri = new LezioneBean();

        // Filtri che forzano l'inesistenza
        filtri.setTipoLezione("SportInesistente");
        filtri.setLivello("Avanzato");

        // Inizializzazione obbligatoria per evitare NullPointerException nel DAO/Query
        filtri.setTariffa(0.0f);
        filtri.setFasciaOraria(""); // Stringa vuota invece di null

        // Inizializziamo la lista dei giorni (tutti false = nessun giorno)
        List<Boolean> giorniVuoti = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
        filtri.setGiorni(giorniVuoti);

        // 2. Act & Assert
        LezioniNonTrovateException exception = assertThrows(LezioniNonTrovateException.class, () -> {
            controller.ricercaLezione(filtri);
        }, "Il sistema dovrebbe lanciare LezioniNonTrovateException");

        // 3. Verifica messaggio
        assertEquals("Nessuna lezione trovata con i filtri inseriti.", exception.getMessage());

        Stampa.println("✅ Test superato: il sistema gestisce correttamente i filtri senza risultati.");
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
    @Test
    void testControllaEmailUtenteInesistente() {
        // Arrange: Usiamo dati che sappiamo non essere presenti nel DB
        String nomeInesistente = "Utente";
        String cognomeInesistente = "Fantasma";
        String emailInesistente = "non-esisto@test.it";

        // Act & Assert: Verifichiamo che il controller lanci l'eccezione corretta
        assertThrows(UtentenonpresenteException.class, () -> {
            controller.controllaEmail(nomeInesistente, cognomeInesistente, emailInesistente);
        }, "Dovrebbe lanciare UtentenonpresenteException se l'utente non è nel DB");

        Stampa.println("✅ Test superato: il controllo email fallisce correttamente per utenti inesistenti.");
    }
}