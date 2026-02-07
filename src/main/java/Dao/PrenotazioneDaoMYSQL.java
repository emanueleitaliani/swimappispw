package Dao;

import Exceptions.UtentenonpresenteException;
import Model.PrenotazioneModel;
import Other.Connect;
import Other.Stampa;
import Other.StatoPrenotazione;
import Query.QueryLezioni;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;


public class PrenotazioneDaoMYSQL implements PrenotazioneDao {
    public void prenota(PrenotazioneModel prenotazioneModel) {
        /*
        fa la query per inserire la richiesta di ripetizione nel database
         */
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            QueryLezioni.PrenotaLezione(stmt, prenotazioneModel);

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            // Chiusura delle risorse
            closeResources(stmt, null);
        }
    }
    public List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) throws SQLException, UtentenonpresenteException {
        List<PrenotazioneModel> listaPrenotazioni = new ArrayList<>();
        Connection connection;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();
            rs = QueryLezioni.cercaPrenotazioniUser(stmt, emailUtente);

            while (rs.next()) {
                // DAO lavora con il MODEL
                PrenotazioneModel model = new PrenotazioneModel();

                model.setIdPrenotazione(rs.getInt("idprenotazione"));
                model.setNome(rs.getString("nomeIstruttore"));
                model.setCognome(rs.getString("cognomeIstruttore"));
                model.setEmailIstruttore(rs.getString("mailistruttore"));
                model.setEmailUtente(rs.getString("mailutente"));
                model.setPrezzo(rs.getFloat("prezzo"));
                model.setGiorno(rs.getString("giorno"));
                model.setInfo(rs.getString("info"));
                model.setOra(rs.getFloat("ora"));

                String statoDalDB = rs.getString("status");
                if (statoDalDB != null) {
                    try {
                        model.setStatus(StatoPrenotazione.valueOf(statoDalDB.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        model.setStatus(StatoPrenotazione.INCORSO);
                    }
                } else {
                    model.setStatus(StatoPrenotazione.INCORSO);
                }

                listaPrenotazioni.add(model);
            }
        } finally {
            if (rs != null) rs.close();
            closeResources(stmt, null);
        }
        return listaPrenotazioni;
    }






    public boolean deletePrenotazioneById(int idPrenotazione,String mailUtente)  {
        Connection connection;
        Statement stmt;
        boolean cancellata=false;


        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();
            QueryLezioni.Cancellaprenotazione(stmt, idPrenotazione, mailUtente);
            cancellata = true;
        } catch(UtentenonpresenteException f){
            Stampa.println("❌ Utente non presente: ");
        } catch (SQLException e) {
            handleDAOException(e);  // Gestione centralizzata dell'eccezione
        }

        return cancellata;
    }
    public List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) throws SQLException {
        List<PrenotazioneModel> lista = new ArrayList<>();
        Connection connection;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            // Chiamata alla classe Query per recuperare le prenotazioni pendenti
            rs = QueryLezioni.cercaPrenotazioniIstruttoreInCorso(stmt, emailIstruttore);

            while (rs.next()) {
                PrenotazioneModel model = new PrenotazioneModel();
                model.setIdPrenotazione(rs.getInt("idprenotazione"));
                model.setEmailUtente(rs.getString("mailutente"));
                model.setEmailIstruttore(rs.getString("mailistruttore"));
                model.setNome(rs.getString("nomeIstruttore"));
                model.setCognome(rs.getString("cognomeIstruttore"));
                model.setPrezzo(rs.getFloat("prezzo"));
                model.setGiorno(rs.getString("giorno"));
                model.setInfo(rs.getString("info"));
                model.setOra(rs.getFloat("ora"));

                String statoDalDb = rs.getString("status");

                // Convertiamo la stringa nell'Enum StatoPrenotazione
                if (statoDalDb != null) {
                    model.setStatus(StatoPrenotazione.valueOf(statoDalDb.toUpperCase()));
                }

                lista.add(model);
            }
        } finally {
            closeResources(stmt, rs);
        }
        return lista;
    }


    public void updateStato(int idPrenotazione, StatoPrenotazione nuovoStato) throws SQLException {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            // Chiamata alla classe Query per l'update effettivo
            QueryLezioni.aggiornaStatoPrenotazione(stmt, idPrenotazione, nuovoStato);

        } finally {
            closeResources(stmt, null);
        }
    }
    public boolean isGiaPrenotata(String emailUtente, String emailIstruttore, String giorno, float ora) throws SQLException {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            // Chiamata alla classe QueryLezioni per la logica effettiva
            return QueryLezioni.isGiaPrenotata(stmt, emailUtente, emailIstruttore, giorno, ora);

        } finally {
            // Chiude lo statement (il ResultSet si chiude automaticamente con esso)
            closeResources(stmt, null);
        }
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
