package testing;

import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Controller.Logincontroller;
import Exceptions.CredenzialisbagliateException;
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
    void testLoginSuccesso() throws CredenzialisbagliateException, UtentenonpresenteException {
        // Arrange
        CredenzialiBean credenziali = new CredenzialiBean("Leoboria11@gmail.com", "Leoboriaa");

        // Act
        Utenteloggatobean risultato = loginController.login(credenziali);

        // Assert
        assertNotNull(risultato, "Il login deve restituire un bean");
        // Nota: Se Actual è null, controlla che nel DB la colonna 'Nome' contenga 'Leo'
        assertEquals("Leo", risultato.getNome(), "Il nome deve corrispondere a quello nel DB");
        Stampa.println("Test Successo completato per: " + risultato.getNome());
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
}