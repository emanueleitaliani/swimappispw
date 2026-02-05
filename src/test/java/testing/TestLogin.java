package testing;

import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Controller.Logincontroller;
import Controller.Registrazionecontroller;
import Exceptions.CredenzialisbagliateException;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Other.Config;
import Other.Stampa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLogin {

    private Logincontroller loginController;

    @BeforeEach
    void setUp() {
        // Forza la persistenza su MYSQL per evitare NullPointerException dalla Factory
        Config.setPersistenceType("mysql");
        loginController = new Logincontroller();
    }

    @Test
    void testLoginSuccesso() {
        // 1. Arrange: Prepariamo i dati
        // Assicurati che nel DB esista: Email: Leoboria11@gmail.com | Pass: Leoboriaa | Nome: Leo
        CredenzialiBean credenziali = new CredenzialiBean("Leoboria11@gmail.com", "Leoboriaa");

        try {
            // 2. Act: Eseguiamo l'azione
            Utenteloggatobean risultato = loginController.login(credenziali);

            // 3. Assert: Verifichiamo i risultati con messaggi di errore chiari

            // Se risultato è null, il test fallisce qui e ti dice perché
            assertNotNull(risultato, "Il login è fallito (restituito null). " +
                    "Verifica che l'utente esista nel DB e che la password sia corretta.");

            // Verifichiamo che il nome sia esattamente quello atteso
            assertEquals("Leo", risultato.getNome(),
                    "Il nome nel Bean (" + risultato.getNome() + ") non corrisponde a 'Leo'. " +
                            "Controlla maiuscole/minuscole nel database.");

            Stampa.println("✅ Test Successo completato per l'utente: " + risultato.getNome());

        } catch (Exception e) {
            // Se il controller o il DAO lanciano un'eccezione non gestita, la catturiamo qui
            fail("Il test ha lanciato un'eccezione imprevista: " + e.getMessage());
        }
    }

    @Test
    void testLoginCredenzialiErrate() throws CredenzialisbagliateException, UtentenonpresenteException {
        CredenzialiBean credenziali = new CredenzialiBean("Leoboria11@gmail.com", "password_errata");
        Utenteloggatobean risultato = loginController.login(credenziali);
        assertNull(risultato, "L'accesso deve essere negato (null)");
    }

    @Test
    void testLoginUtenteInesistente() throws CredenzialisbagliateException, UtentenonpresenteException {
        CredenzialiBean credenziali = new CredenzialiBean("non_esisto_mai@test.it", "1234");
        Utenteloggatobean risultato = loginController.login(credenziali);
        assertNull(risultato, "Utente non presente deve restituire null");
    }
    @Test
    void testRegistrazioneEmailGiaInUso() {
        // Arrange: Usiamo l'email di Leo che esiste già nel DB
        Registrazionecontroller regController = new Registrazionecontroller();

        // Prepariamo un Bean con email duplicata
        CredenzialiBean credenziali = new CredenzialiBean("Leoboria11@gmail.com", "nuovapassword");
        Utenteloggatobean nuovoUtente = new Utenteloggatobean(credenziali, "Marco", "Rossi", false);

        // Act & Assert: Verifichiamo che lanci EmailgiainusoException
        assertThrows(EmailgiainusoException.class, () -> {
            regController.registrazione(nuovoUtente);
        }, "Il controller dovrebbe impedire la registrazione di una email già presente");

        Stampa.println("✅ Test Registrazione Duplicata: OK (Lanciata EmailgiainusoException)");
    }

    @Test
    void testRegistrazioneFlussoIstruttore() {
        Registrazionecontroller regController = new Registrazionecontroller();

        // Creiamo un utente casuale per non andare in conflitto con test precedenti
        String emailCasuale = "istruttore_" + System.currentTimeMillis() + "@test.it";
        CredenzialiBean credenziali = new CredenzialiBean(emailCasuale, "password123");
        Utenteloggatobean istruttore = new Utenteloggatobean(credenziali, "Test", "Istruttore", true);

        // Verifichiamo che la registrazione non lanci eccezioni
        assertDoesNotThrow(() -> {
            regController.registrazione(istruttore);
        }, "La registrazione di un nuovo istruttore dovrebbe avere successo");

        Stampa.println("✅ Test Registrazione Istruttore: OK per " + emailCasuale);
    }
}