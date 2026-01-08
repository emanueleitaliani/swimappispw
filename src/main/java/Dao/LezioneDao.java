package Dao;

import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;

import java.util.List;

public interface LezioneDao {
    List<LezioneModel> cercaLezione(LezioneModel lezioneModel);
    boolean controllaEmail(String nome, String cognome, String email) throws UtentenonpresenteException;
}
