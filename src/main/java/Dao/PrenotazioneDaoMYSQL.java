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
                model.setOra(rs.getString("ora"));

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






    @Override
    public boolean deletePrenotazione(PrenotazioneModel prenotazioneModel) throws SQLException, UtentenonpresenteException {
        Connection connection;
        Statement stmt = null;
        boolean cancellata = false;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            // Eseguiamo la query passando lo stmt e il model
            int rowsAffected = QueryLezioni.cancellaPrenotazione(connection, prenotazioneModel);

            if (rowsAffected > 0) {
                cancellata = true;
            } else {
                // 💡 SOLUZIONE: Se rowsAffected è 0, la prenotazione (o l'utente) non esiste nel DB
                throw new UtentenonpresenteException();
            }

        } catch(UtentenonpresenteException f){
            Stampa.println("❌ Prenotazione o Utente non presente nel sistema.");
            throw f; // Rilancia l'eccezione verso il Controller
        } catch (SQLException e) {
            handleDAOException(e);
            throw e;
        } finally {
            // Buona norma per chiudere sempre lo statement
            if (stmt != null) stmt.close();
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
                model.setOra(rs.getString("ora"));

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



    public void updateStato(PrenotazioneModel prenotazioneModel) throws SQLException {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            // Estraiamo l'ID e lo Stato dall'oggetto Model ricevuto
            int id = prenotazioneModel.getIdPrenotazione();
            StatoPrenotazione nuovoStato = prenotazioneModel.getStatus();

            // Chiamata alla classe Query per l'update effettivo sul DB
            QueryLezioni.aggiornaStatoPrenotazione(stmt, id, nuovoStato);

        } finally {
            closeResources(stmt, null);
        }
    }
    public boolean isGiaPrenotata(String emailUtente, String giorno, String ora) throws SQLException {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();

            return QueryLezioni.isGiaPrenotata(stmt, emailUtente, giorno, ora);

        } finally {
            closeResources(stmt, null);
        }
    }
    @Override
    public boolean isIstruttoreOccupato(String emailIstruttore, String giorno, String ora) throws SQLException {
        Connection connection;
        Statement stmt = null;

        try {
            connection = Connect.getInstance().getDBConnection();
            stmt = connection.createStatement();
            return QueryLezioni.isIstruttoreOccupato(stmt, emailIstruttore, giorno, ora);
        } finally {
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
