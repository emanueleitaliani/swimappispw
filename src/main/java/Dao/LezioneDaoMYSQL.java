package Dao;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Other.Connect;
import Query.QueryLezioni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LezioneDaoMYSQL implements LezioneDao {

    private static final Logger logger = Logger.getLogger(LezioneDaoMYSQL.class.getName());
    public List<LezioneModel> cercaLezione(LezioneModel lezioneModel) {
        List<LezioneModel> risultati = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();
            rs = QueryLezioni.cercaLezione(stmt, lezioneModel);


            while (rs.next()) {
                LezioneModel lezione = new LezioneModel();
                lezione.setEmailIstruttore(rs.getString("email"));
                lezione.setNomeIstruttore(rs.getString("nome"));
                lezione.setCognomeIstruttore(rs.getString("cognome"));
                lezione.setFasciaOraria(rs.getString("fascia_oraria"));
                lezione.setLivello(rs.getString("livello"));
                lezione.setTariffa(rs.getFloat("tariffa"));
                lezione.setTipoLezione(rs.getString("tipo_lezione"));
                lezione.setNote(rs.getString("note"));
                lezione.setGiorniDisponibili(rs.getString("giorni_disponibili"));
                risultati.add(lezione);
            }
        } catch (SQLException e) {
            handleDAOException("Errore", e);// gestione migliorabile
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return risultati;
    }

    public boolean controllaEmail(String nome, String cognome, String email) {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            QueryLezioni.istruttoreEsiste(stmt, nome, cognome, email);
        } catch (UtentenonpresenteException e) {
            handleDAOException("L'istruttore cercato non e' nel sistema", e);
            return false;
        } catch (SQLException e) {
            handleDAOException("Errore SQL durante il controllo dell'email dell'istruttore", e);
            return false;
        } finally {
            // Chiusura delle risorse
            closeResources(stmt, null);
        }
        return true;
    }
    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            handleDAOException("Errore durante la chiusura delle risorse del db",e);
        }
    }

    private void handleDAOException(String contesto, Exception e) {
        logger.severe(String.format("LezioneDaoMYSQL - %s: %s", contesto, e.getMessage()));
    }
}


