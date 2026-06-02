package testing;

import Bean.LezioneBean;
import Bean.Prenotazionebean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioneGiaOccupataException;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.LezioniNonTrovateException;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Other.Config;
import Other.Stampa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class TestUtente {

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
         // 1. Arrange
         Prenotazionebean p = new Prenotazionebean();

         // Usiamo un'email dinamica basata sul tempo corrente per evitare che il test
         // fallisca se eseguito più volte consecutivamente sul database reale
         String emailUnicaUtente = "utente." + System.currentTimeMillis() + "@test.it";

         p.setEmailUser(emailUnicaUtente);
         p.setEmailIstruttore("coach1@test.com"); // Istruttore esistente nel sistema
         p.setGiorno("lunedì");                  // Normalizzato in minuscolo
         p.setHour("09-11");                     // RISOLTO: Stringa coerente con il Bean e la fascia oraria del DB

         // 2. Act & Assert
         // La prima prenotazione deve andare a buon fine senza lanciare eccezioni
         assertDoesNotThrow(() -> {
             controller.richiediprenotazione(p);
         }, "La prima prenotazione di uno slot libero dovrebbe essere accettata.");

         // La seconda prenotazione IDENTICA deve essere bloccata dal controller
         assertThrows(LezioneGiaPrenotataException.class, () -> {
             controller.richiediprenotazione(p);
         }, "Il sistema dovrebbe impedire la duplicazione lanciando LezioneGiaPrenotataException");

         Stampa.println("✅ Test superato: il controller impedisce correttamente la doppia prenotazione dello stesso slot.");
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

     @Test
     void testRichiediPrenotazioneSuLezionePrivataGiaOccupata() {
         // 1. Arrange: Usiamo i dati ESATTI della lezione privata presente nel tuo DB
         String giornoLezione = "lunedì";
         String fasciaOraria = "10-12";
         String emailIstruttore = "ercogoal22@gmail.com";
         long timestamp = System.currentTimeMillis();

         // Primo utente che prenota la lezione privata di Mattia
         Prenotazionebean primoCliente = new Prenotazionebean();
         primoCliente.setIdPrenotazione(2001); // ID manuale univoco per evitare conflitti
         primoCliente.setEmailUser("clienteA." + timestamp + "@test.it"); // Email dinamica
         primoCliente.setEmailIstruttore(emailIstruttore);
         primoCliente.setGiorno(giornoLezione);
         primoCliente.setHour(fasciaOraria);
         primoCliente.setNome("Mario");
         primoCliente.setCognome("Rossi");
         primoCliente.setPrezzo(35.0f); // Prezzo coerente con la lezione

         // Secondo utente che tenta di prenotare lo stesso identico slot privato
         Prenotazionebean secondoCliente = new Prenotazionebean();
         secondoCliente.setIdPrenotazione(2002); // ID manuale univoco distinto
         secondoCliente.setEmailUser("clienteB." + (timestamp + 10) + "@test.it");
         secondoCliente.setEmailIstruttore(emailIstruttore);
         secondoCliente.setGiorno(giornoLezione);
         secondoCliente.setHour(fasciaOraria);
         secondoCliente.setNome("Luigi");
         secondoCliente.setCognome("Verdi");
         secondoCliente.setPrezzo(35.0f);

         // 2. Act & Assert
         // La prima prenotazione deve andare a buon fine e salvarsi sulla tabella `prenotazione`
         assertDoesNotThrow(() -> {
             controller.richiediprenotazione(primoCliente);
         }, "La prima prenotazione sulla lezione privata esistente deve essere accettata.");

         // La seconda deve fallire! Ora la JOIN troverà sia il record della prenotazione appena fatto,
         // sia la lezione di Mattia che è marcata come 'privata'. La query restituirà COUNT > 0.
         assertThrows(LezioneGiaOccupataException.class, () -> {
             controller.richiediprenotazione(secondoCliente);
         }, "Il sistema deve lanciare LezioneGiaOccupataException perché lo slot privato è già occupato.");

         Stampa.println("✅ Test superato: agganciata la lezione reale sul DB e bloccata la prenotazione concorrente.");
     }

}