package Query;
import Exceptions.EmailnonvalidaException;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Model.PrenotazioneModel;
import Other.Stampa;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import Bean.Prenotazionebean;
import Other.StatoPrenotazione;

public class QueryLezioni {
    private QueryLezioni(){

    }

    public static void PrenotaLezione(Statement smt, PrenotazioneModel prenotazione){
        try{
            int idprenotazione= prenotazione.getIdPrenotazione();
            String nomeIstruttore= prenotazione.getNome();
            String cognomeIstruttore= prenotazione.getCognome();
            String emailistruttore= prenotazione.getEmailIstruttore();
            String emailutente= prenotazione.getEmailUtente();
            float prezzo= prenotazione.getPrezzo();
            String giorno=prenotazione.getGiorno();
            String info =prenotazione.getInfo();
            float ora=prenotazione.getOra();

            String richiesta=String.format(Locale.US,Query.INSERISCIPRENOTAZIONE,idprenotazione,nomeIstruttore, cognomeIstruttore,emailutente,emailistruttore,prezzo,giorno,info,ora);
            smt.executeUpdate(richiesta);
        } catch(SQLException e){
            handleException(e);
        }
    };

    public static boolean istruttoreEsiste(Statement smt,String nome,String cognome,String emailIstruttore) throws UtentenonpresenteException {
        try{
            String richiesta2=String.format(Query.SEARCHISTRUCTOR,nome,cognome,emailIstruttore);
            ResultSet rs = smt.executeQuery(richiesta2);


            if (!rs.next()){
                throw new UtentenonpresenteException();

            }

        } catch (SQLException e) {
            handleException(e);  // Il tuo metodo personalizzato per log o gestione errori
            return false;
        }
        return true;
    }
    public static ResultSet cercaPrenotazioniUser(Statement stmt, String emailUtente) throws SQLException,UtentenonpresenteException {
        String richiesta = String.format(Query.CERCA_PRENOTAZIONI, emailUtente);
        ResultSet rs = stmt.executeQuery(richiesta);

        if (!rs.isBeforeFirst()) { // Nessuna riga presente
            throw new UtentenonpresenteException();
        }

        return rs;

    }
    public static void Cancellaprenotazione(Statement stmt,int IdPrenotazione,String mailUtente) throws SQLException,UtentenonpresenteException {
        String richiesta = String.format(Query.CANCELLA_PRENOTAZIONE,IdPrenotazione,mailUtente);
        stmt.executeUpdate(richiesta);
    }
    public static ResultSet cercaLezione(Statement smt, LezioneModel filtri){
        String sql;
        ResultSet rs=null;
        try {
            sql = String.format(Query.RICERCA_LEZIONI_BASE, filtri.getTariffa());

            // Base query: solo filtro su tariffa se presente
            if (filtri.getTariffa() > 0) {
                sql = String.format(Locale.US,"SELECT * FROM lezioni WHERE tariffa <= %.2f", filtri.getTariffa());
            } else {
                sql = "SELECT * FROM lezioni WHERE 1=1"; // fallback senza tariffa
            }

            // Fascia oraria
            if (filtri.getFasciaOraria() != null && !filtri.getFasciaOraria().isEmpty()) {
                sql += String.format(" AND fascia_oraria = '%s'", filtri.getFasciaOraria());
            }

            // Livello
            if (filtri.getLivello() != null && !filtri.getLivello().isEmpty()) {
                sql += String.format(" AND livello = '%s'", filtri.getLivello());
            }

            // Tipo lezione
            if (filtri.getTipoLezione() != null && !filtri.getTipoLezione().isEmpty()) {
                sql += String.format(" AND tipo_lezione = '%s'", filtri.getTipoLezione());
            }

            // Note (LIKE)
            if (filtri.getNote() != null && !filtri.getNote().isEmpty()) {
                sql += String.format(" AND LOWER(note) LIKE '%%%s%%'", filtri.getNote().toLowerCase());
            }

            // Giorni disponibili (LIKE)
            if (filtri.getGiorniDisponibili() != null && !filtri.getGiorniDisponibili().isEmpty()) {
                sql += String.format(" AND LOWER(giorni_disponibili) LIKE '%%%s%%'", filtri.getGiorniDisponibili().toLowerCase());
            }

            // Debug (opzionale)
            Stampa.println("Query generata: " + sql);

            rs = smt.executeQuery(sql);
        } catch (SQLException e) {
            handleException(e); // o stampa errore/logging
        }

        return rs;
    }
    public static ResultSet cercaPrenotazioniIstruttoreInCorso(Statement stmt, String emailIstruttore) throws SQLException {

        String richiesta = String.format(Query.CERCA_PRENOTAZIONI_ISTRUTTORE, emailIstruttore);
        return stmt.executeQuery(richiesta);
    }

    // --- NUOVO METODO: Aggiorna lo stato della prenotazione ---
    public static void aggiornaStatoPrenotazione(Statement stmt, int id, StatoPrenotazione nuovoStato) throws SQLException {
        // Utilizziamo una nuova costante che dovrai aggiungere in Query.java
        // Esempio: "UPDATE prenotazione SET status = '%s' WHERE idprenotazione = %d"
        String richiesta = String.format(Query.AGGIORNA_STATO_PRENOTAZIONE,nuovoStato.name(), id);
        stmt.executeUpdate(richiesta);
    }
    public static boolean isGiaPrenotata(Statement stmt, String emailUtente, String emailIstruttore, String giorno, float ora) throws SQLException {
        try {
            String richiesta = String.format(Locale.US, Query.VERIFICA_GIA_PRENOTATA,
                    emailUtente, emailIstruttore, giorno, ora);
            ResultSet rs = stmt.executeQuery(richiesta);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return false;
    }



    private static void handleException(Exception e) {
        Stampa.errorPrint(String.format("QueryPrenotazione: %s", e.getMessage()));
    }


}