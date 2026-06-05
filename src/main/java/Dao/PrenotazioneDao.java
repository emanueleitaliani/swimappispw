package Dao;


import Exceptions.UtentenonpresenteException;
import Model.PrenotazioneModel;


import java.sql.SQLException;
import java.util.List;

public interface PrenotazioneDao {
    void prenota(PrenotazioneModel prenotazioneModel) throws SQLException;

    List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) throws SQLException;

    boolean deletePrenotazione(PrenotazioneModel prenotazioneModel) throws SQLException, UtentenonpresenteException;

    List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) throws SQLException;

    void updateStato(PrenotazioneModel prenotazioneModel) throws SQLException;

    boolean isGiaPrenotata(String emailUtente, String giorno, String ora) throws SQLException;

    boolean isIstruttoreOccupato(String emailIstruttore, String giorno, String ora) throws SQLException;
}
