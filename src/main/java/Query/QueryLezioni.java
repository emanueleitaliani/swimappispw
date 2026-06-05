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
    }

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
    public static ResultSet cercaPrenotazioniUser(Statement stmt, String emailUtente) throws SQLException {
        String richiesta = String.format(Query.CERCA_PRENOTAZIONI, emailUtente);
        ResultSet rs = stmt.executeQuery(richiesta);

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

        // 1. Query base (Tariffa)
        if (filtri.getTariffa() != null && filtri.getTariffa() > 0) {
            // Sostituito il testo a mano con la costante di Query.java
            queryBuilder.append(String.format(Locale.US, Query.RICERCA_LEZIONI_BASE, filtri.getTariffa()));
        } else {
            queryBuilder.append("SELECT * FROM lezioni WHERE 1=1");
        }

        // 2. Filtri stringa standard
        appendFiltroEquals(queryBuilder, "fascia_oraria", filtri.getFasciaOraria());
        appendFiltroEquals(queryBuilder, "livello", filtri.getLivello());
        appendFiltroEquals(queryBuilder, "tipo_lezione", filtri.getTipoLezione());

        // 3. Filtro Note (LIKE)
        if (filtri.getNote() != null && !filtri.getNote().isEmpty()) {
            queryBuilder.append(String.format(" AND LOWER(note) LIKE '%%%s%%'", filtri.getNote().toLowerCase()));
        }

        // 4. Filtro Giorni (Logica complessa estratta)
        appendFiltroGiorni(queryBuilder, filtri.getGiorniDisponibili());

        // 5. Esecuzione finale
        try {
            return smt.executeQuery(queryBuilder.toString());
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }


    private static void appendFiltroGiorni(StringBuilder queryBuilder, String giorniDisponibili) {
        if (giorniDisponibili == null || giorniDisponibili.isEmpty()) {
            return;
        }

        String[] giorniSelezionati = giorniDisponibili.split(",");
        queryBuilder.append(" AND (");

        for (int i = 0; i < giorniSelezionati.length; i++) {
            String giornoPulito = giorniSelezionati[i].trim().toLowerCase();
            queryBuilder.append(String.format("LOWER(giorni_disponibili) = '%s'", giornoPulito));

            if (i < giorniSelezionati.length - 1) {
                queryBuilder.append(" OR ");
            }
        }
        queryBuilder.append(")");
    }


    private static void appendFiltroEquals(StringBuilder queryBuilder, String colonna, String valore) {
        if (valore != null && !valore.isEmpty()) {
            queryBuilder.append(String.format(" AND %s = '%s'", colonna, valore));
        }
    }
    public static ResultSet cercaPrenotazioniIstruttoreInCorso(Statement stmt, String emailIstruttore) throws SQLException {

        String richiesta = String.format(Query.CERCA_PRENOTAZIONI_ISTRUTTORE, emailIstruttore);
        return stmt.executeQuery(richiesta);
    }


    public static void aggiornaStatoPrenotazione(Statement stmt, int id, StatoPrenotazione nuovoStato) throws SQLException {

        String richiesta = String.format(Query.AGGIORNA_STATO_PRENOTAZIONE,nuovoStato.name(), id);
        stmt.executeUpdate(richiesta);
    }
    public static boolean isGiaPrenotata(Connection conn, String emailUtente, String giorno, String ora) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(Query.VERIFICA_GIA_PRENOTATA)) {

            // Assegnazione dei parametri in modo sicuro (sostituiscono i '?' nella query)
            pstmt.setString(1, emailUtente.trim());
            pstmt.setString(2, giorno.trim().toLowerCase());
            pstmt.setString(3, ora.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
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
    public static boolean isIstruttoreOccupato(Connection conn, String emailIstruttore, String giorno, String ora) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(Query.VERIFICA_ISTRUTTORE_OCCUPATO)) {

            // Assegnazione dei parametri in modo sicuro
            pstmt.setString(1, emailIstruttore.trim());
            pstmt.setString(2, giorno.trim().toLowerCase());
            pstmt.setString(3, ora.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
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