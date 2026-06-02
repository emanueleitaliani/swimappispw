package Query;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Model.PrenotazioneModel;
import Other.Stampa;

import java.sql.*;

import java.util.Locale;

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
            String ora=prenotazione.getOra();

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

    public static int cancellaPrenotazione(Connection conn, PrenotazioneModel prenotazione) throws SQLException {
        int id = prenotazione.getIdPrenotazione();
        String mailUtente = prenotazione.getEmailUtente();

        // Prepariamo la query in modo sicuro tramite il PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(Query.CANCELLA_PRENOTAZIONE)) {
            // Associazioni dei parametri in base all'ordine dei '?' nella query
            pstmt.setInt(1, id);          // Sostituisce il primo '?' con l'id (intero)
            pstmt.setString(2, mailUtente); // Sostituisce il secondo '?' con la mail (stringa)

            // Esegue l'aggiornamento in sicurezza e ritorna il numero di righe eliminate
            return pstmt.executeUpdate();
        }
    }
    public static ResultSet cercaLezione(Statement smt, LezioneModel filtri) {
        StringBuilder queryBuilder = new StringBuilder();

        // 1. Applichiamo la query base a seconda che ci sia o meno la tariffa massima
        if (filtri.getTariffa() != null && filtri.getTariffa() > 0) {
            queryBuilder.append(String.format(Locale.US, "SELECT * FROM lezioni WHERE tariffa <= %.2f", filtri.getTariffa()));
        } else {
            queryBuilder.append("SELECT * FROM lezioni WHERE 1=1");
        }

        // 2. Filtro dinamico e generico sulla Fascia Oraria
        if (filtri.getFasciaOraria() != null && !filtri.getFasciaOraria().isEmpty()) {
            queryBuilder.append(String.format(" AND fascia_oraria = '%s'", filtri.getFasciaOraria()));
        }

        // 3. Filtro dinamico e generico sul Livello
        if (filtri.getLivello() != null && !filtri.getLivello().isEmpty()) {
            queryBuilder.append(String.format(" AND livello = '%s'", filtri.getLivello()));
        }

        // 4. Filtro dinamico e generico sul Tipo Lezione (es. Privata / In Gruppo)
        if (filtri.getTipoLezione() != null && !filtri.getTipoLezione().isEmpty()) {
            queryBuilder.append(String.format(" AND tipo_lezione = '%s'", filtri.getTipoLezione()));
        }

        // 5. Filtro dinamico sulle Note (usando LIKE in minuscolo per evitare problemi di maiuscole)
        if (filtri.getNote() != null && !filtri.getNote().isEmpty()) {
            queryBuilder.append(String.format(" AND LOWER(note) LIKE '%%%s%%'", filtri.getNote().toLowerCase()));
        }

        // 6. GESTIONE GENERICA E AUTOMATICA DEI GIORNI MULTIPLI (Risolve il problema del Lunedì/Mercoledì)
        if (filtri.getGiorniDisponibili() != null && !filtri.getGiorniDisponibili().isEmpty()) {
            // "Lunedì, Mercoledì" viene diviso in un array di stringhe: ["Lunedì", " Mercoledì"]
            String[] giorniSelezionati = filtri.getGiorniDisponibili().split(",");

            queryBuilder.append(" AND (");

            for (int i = 0; i < giorniSelezionati.length; i++) {
                // Rimuove gli spazi prima/dopo e trasforma tutto in minuscolo (es: " Mercoledì" -> "mercoledì")
                String giornoPulito = giorniSelezionati[i].trim().toLowerCase();

                queryBuilder.append(String.format("LOWER(giorni_disponibili) = '%s'", giornoPulito));

                // Se ci sono altri giorni selezionati dopo questo, aggiunge la clausola OR
                if (i < giorniSelezionati.length - 1) {
                    queryBuilder.append(" OR ");
                }
            }

            queryBuilder.append(")");
        }

        // 7. Esecuzione finale della query generata al volo
        try {
            return smt.executeQuery(queryBuilder.toString());
        } catch (SQLException e) {
            handleException(e); // Mantieni il tuo metodo di gestione degli errori
            return null;
        }
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
    public static boolean isGiaPrenotata(Statement stmt, String emailUtente, String giorno, String ora) throws SQLException {
        try {
            // Passiamo solo emailUtente, giorno e ora alla stringa SQL modificata
            String req = String.format(Locale.US, Query.VERIFICA_GIA_PRENOTATA,
                    emailUtente, giorno, ora);

            try (ResultSet rs = stmt.executeQuery(req)) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            handleException(e);
            throw e; // È buona pratica rilanciarla per far sapere al DAO che qualcosa è fallito
        }
        return false;
    }
    public static boolean isIstruttoreOccupato(Statement stmt, String emailIstruttore, String giorno, String ora) throws SQLException {
        try {
            String request = String.format(Locale.US, Query.VERIFICA_ISTRUTTORE_OCCUPATO,
                    emailIstruttore, giorno, ora);

            try (ResultSet rs = stmt.executeQuery(request)) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
        return false;
    }



    private static void handleException(Exception e) {
        Stampa.errorPrint(String.format("QueryPrenotazione: %s", e.getMessage()));
    }


}