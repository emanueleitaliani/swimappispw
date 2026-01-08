package Query;

public class Query {

    private Query() {
        // Costruttore privato per evitare istanziazione
    }

    // Query per cercare se un'email esiste (usata sia nel login che nella registrazione)
    public static final String RICERCA_EMAIL =
            "SELECT email FROM utente WHERE email = '%s'";

    // Query per verificare email e password per il login
    public static final String VERIFICA_USER =
            "SELECT * FROM utente WHERE email = '%s' AND password = '%s'";

    // Query per registrare un nuovo utente
    public static final String REGISTRAZIONE =
            "INSERT INTO utente (nome, cognome,email, IsIstructor ,password) VALUES ('%s', '%s', '%s', %b,'%s')";

    public static final String INSERISCIPRENOTAZIONE =
            "INSERT INTO prenotazione(idprenotazione,nomeIstruttore,cognomeIstruttore,mailutente,mailistruttore,prezzo,giorno,info,ora) VALUES ('%d','%s','%s','%s','%s','%f','%s','%s','%f')";

    public static final String SEARCHISTRUCTOR =
            "SELECT * FROM utente WHERE nome = '%s' AND cognome = '%s' AND email = '%s' AND IsIstructor is True ";
    public static final String CERCA_PRENOTAZIONI =
            "SELECT * FROM prenotazione WHERE mailutente = '%s'";
    public static final String CERCA_PRENOTAZIONI_ISTRUTTORE =
            "SELECT * FROM prenotazione WHERE mailistruttore = '%s' AND status = 'incorso'";

    // Aggiorna lo stato di una specifica prenotazione
    public static final String AGGIORNA_STATO_PRENOTAZIONE =
            "UPDATE prenotazione SET status = '%s' WHERE idprenotazione = %d";
    public static final String CANCELLA_PRENOTAZIONE =
            "DELETE FROM prenotazione WHERE idprenotazione = '%d' AND mailutente = '%s'";
    public static final String RICERCA_LEZIONI_BASE = "SELECT * FROM lezioni WHERE tariffa <= %.2f";

    public static final String VERIFICA_GIA_PRENOTATA =
            "SELECT COUNT(*) FROM prenotazione WHERE mailutente = '%s' AND mailistruttore = '%s' AND giorno = '%s' AND ora = %f";
}



