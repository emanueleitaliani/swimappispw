package Dao;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Other.Connect;
import Other.Stampa;
import Query.QueryLezioni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LezioneDaoMYSQL implements LezioneDao {

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
            e.printStackTrace(); // gestione migliorabile
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
            handleDAOException(e);
            return false;
        } catch (SQLException e) {

            handleDAOException(e);
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
            handleDAOException(e);
        }
    }


    private void handleDAOException(Exception e) {
        Stampa.errorPrint(String.format("PrenotazioneDAO: %s", e.getMessage()));
    }
}


