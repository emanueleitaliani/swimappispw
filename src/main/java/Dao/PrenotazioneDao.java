package Dao;


import Exceptions.UtentenonpresenteException;
import Model.PrenotazioneModel;


import java.sql.SQLException;
import java.util.List;

public interface PrenotazioneDao {
    void prenota(PrenotazioneModel prenotazioneModel) throws SQLException;

    List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) throws SQLException, UtentenonpresenteException;

    boolean deletePrenotazione(PrenotazioneModel prenotazioneModel) throws SQLException, UtentenonpresenteException;

    List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) throws SQLException;

    void updateStato(PrenotazioneModel prenotazioneModel) throws SQLException;

    boolean isGiaPrenotata(String emailUtente, String emailIstruttore, String giorno, float ora) throws SQLException;
}
