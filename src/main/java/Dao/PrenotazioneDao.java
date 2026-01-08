package Dao;

import Bean.Prenotazionebean;
import Exceptions.UtentenonpresenteException;
import Model.PrenotazioneModel;
import Other.StatoPrenotazione;

import java.sql.SQLException;
import java.util.List;

public interface PrenotazioneDao {
    public void prenota(PrenotazioneModel prenotazioneModel) throws SQLException;

    public List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) throws SQLException, UtentenonpresenteException;

    public boolean deletePrenotazioneById(int IdPrenotazione, String mailUtente) throws SQLException, UtentenonpresenteException;

    public List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) throws SQLException;

    public void updateStato(int idPrenotazione, StatoPrenotazione nuovoStato) throws SQLException;

    public boolean isGiaPrenotata(String emailUtente, String emailIstruttore, String giorno, float ora) throws SQLException;
}
