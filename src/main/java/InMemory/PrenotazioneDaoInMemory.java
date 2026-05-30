package InMemory;

import Dao.PrenotazioneDao;
import Model.PrenotazioneModel;
import Other.StatoPrenotazione;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrenotazioneDaoInMemory implements PrenotazioneDao {

    // La lista deve contenere solo MODEL
    private static final List<PrenotazioneModel> prenotazioni = new ArrayList<>();
    private final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    @Override
    public void prenota(PrenotazioneModel model) {
        // Assegniamo l'ID direttamente al Model prima di salvarlo
        model.setIdPrenotazione(ID_GENERATOR.getAndIncrement());

        // Se lo stato è null, impostiamolo come INCORSO di default
        if (model.getStatus() == null) {
            model.setStatus(StatoPrenotazione.INCORSO);
        }

        prenotazioni.add(model);
    }

    @Override
    public List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) {
        List<PrenotazioneModel> risultati = new ArrayList<>();
        for (PrenotazioneModel p : prenotazioni) {
            // Usiamo getEmailUtente() che è il nome corretto nel Model
            if (p.getEmailUtente().equalsIgnoreCase(emailUtente)) {
                risultati.add(p);
            }
        }
        return risultati;
    }
    @Override
    public boolean deletePrenotazione(PrenotazioneModel prenotazioneModel) {
        return prenotazioni.removeIf(p ->
                p.getIdPrenotazione() == prenotazioneModel.getIdPrenotazione() &&
                        p.getEmailUtente().equalsIgnoreCase(prenotazioneModel.getEmailUtente())
        );
    }

    @Override
    public List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) {
        List<PrenotazioneModel> risultati = new ArrayList<>();
        for (PrenotazioneModel p : prenotazioni) {
            if (p.getEmailIstruttore().equalsIgnoreCase(emailIstruttore)) {
                // Non serve convertire: la lista contiene già Model!
                risultati.add(p);
            }
        }
        return risultati;
    }

    @Override
    public void updateStato(PrenotazioneModel prenotazioneModel) {
        for (PrenotazioneModel p : prenotazioni) {
            // Cerchiamo la prenotazione in memoria tramite l'ID del model passato
            if (p.getIdPrenotazione() == prenotazioneModel.getIdPrenotazione()) {
                // Aggiorniamo lo stato in memoria prendendolo dal model aggiornato
                p.setStatus(prenotazioneModel.getStatus());
                return; // Usciamo dal ciclo appena effettuato l'aggiornamento
            }
        }
    }
    @Override
    public boolean isGiaPrenotata(String emailUtente, String emailIstruttore, String giorno, float ora) {
        for (PrenotazioneModel p : prenotazioni) {

            boolean stessaEmailUtente = p.getEmailUtente().equalsIgnoreCase(emailUtente);
            boolean stessaEmailIstruttore = p.getEmailIstruttore().equalsIgnoreCase(emailIstruttore);
            boolean stessoGiorno = p.getGiorno().equalsIgnoreCase(giorno);

            boolean stessaOra = Float.compare(p.getOra(), ora) == 0;

            if (stessaEmailUtente && stessaEmailIstruttore && stessoGiorno && stessaOra) {
                return true; // Trovata corrispondenza, la lezione è già prenotata
            }
        }
        return false; // Nessuna corrispondenza trovata
    }

}
