package testing;
import Other.Connect;
import Other.Stampa;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

 class TestDBConnection {

    @Test
     void testDatabaseConnection() {
        try {
            // Otteniamo la connessione dal Singleton
            Connection conn = Connect.getInstance().getDBConnection();

            // 1. Verifichiamo che la connessione non sia null
            assertNotNull(conn, "La connessione non dovrebbe essere null");

            // 2. Verifichiamo che sia aperta
            assertFalse(conn.isClosed(), "La connessione dovrebbe essere aperta");

            // 3. Test funzionale con query leggera
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {

                assertTrue(rs.next(), "Il ResultSet dovrebbe avere almeno un risultato");
                Stampa.println("✅ Database Test: OK");
            }

        } catch (Exception e) {
            Stampa.errorPrint("❌ Errore durante il test: " + e.getMessage());
            fail("Il test è fallito a causa di un'eccezione");
        }
    }
}
