package InMemory;

import Dao.PrenotazioneDao;
import Model.LezioneModel;
import Model.PrenotazioneModel;
import Other.StatoPrenotazione;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrenotazioneDaoInMemory implements PrenotazioneDao {

    // La lista deve contenere solo MODEL

    private final AtomicInteger ID_GENERATOR = new AtomicInteger(1);


    @Override
    public void prenota(PrenotazioneModel model) {
        model.setIdPrenotazione(ID_GENERATOR.getAndIncrement());

        if (model.getStatus() == null) {
            model.setStatus(StatoPrenotazione.INCORSO);
        }

        LocalDatabase.PRENOTAZIONI.add(model);
    }

    @Override
    public List<PrenotazioneModel> getPrenotazioniByEmail(String emailUtente) {
        List<PrenotazioneModel> risultati = new ArrayList<>();
        for (PrenotazioneModel p : LocalDatabase.PRENOTAZIONI) {
            if (p.getEmailUtente().equalsIgnoreCase(emailUtente)) {
                risultati.add(p);
            }
        }
        return risultati;
    }
    @Override
    public boolean deletePrenotazione(PrenotazioneModel prenotazioneModel) {
        return LocalDatabase.PRENOTAZIONI.removeIf(p ->
                p.getIdPrenotazione() == prenotazioneModel.getIdPrenotazione() &&
                        p.getEmailUtente().equalsIgnoreCase(prenotazioneModel.getEmailUtente())
        );
    }

    @Override
    public List<PrenotazioneModel> getPrenotazioniPerIstruttore(String emailIstruttore) {
        List<PrenotazioneModel> risultati = new ArrayList<>();
        for (PrenotazioneModel p : LocalDatabase.PRENOTAZIONI) {
            if (p.getEmailIstruttore().equalsIgnoreCase(emailIstruttore)) {
                risultati.add(p);
            }
        }
        return risultati;
    }

    @Override
    public void updateStato(PrenotazioneModel prenotazioneModel) {
        for (PrenotazioneModel p : LocalDatabase.PRENOTAZIONI) {
            if (p.getIdPrenotazione() == prenotazioneModel.getIdPrenotazione()) {
                p.setStatus(prenotazioneModel.getStatus());
                return;
            }
        }
    }

    @Override
    public boolean isGiaPrenotata(String emailUtente, String giorno, String ora) {
        for (PrenotazioneModel p : LocalDatabase.PRENOTAZIONI) {
            boolean stessaEmailUtente = p.getEmailUtente().equalsIgnoreCase(emailUtente);
            boolean stessoGiorno = p.getGiorno().equalsIgnoreCase(giorno);
            boolean stessaOra = p.getOra().equalsIgnoreCase(ora);

            if (stessaEmailUtente && stessoGiorno && stessaOra) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isIstruttoreOccupato(String emailIstruttore, String giorno, String ora) {
        for (PrenotazioneModel p : LocalDatabase.PRENOTAZIONI) {

            // Verifichiamo se l'istruttore specifico ha già un impegno non rifiutato in quel momento
            boolean stessoIstruttore = p.getEmailIstruttore().equalsIgnoreCase(emailIstruttore);
            boolean stessoGiorno = p.getGiorno().equalsIgnoreCase(giorno);
            boolean stessaOra = p.getOra().equalsIgnoreCase(ora);
            boolean nonRifiutata = p.getStatus() != StatoPrenotazione.RIFIUTATA;

            if (stessoIstruttore && stessoGiorno && stessaOra && nonRifiutata) {

                // 🧩 SIMULAZIONE JOIN: Trovata una prenotazione, andiamo a vedere se la lezione sorgente è "Privata"
                for (LezioneModel l : LocalDatabase.LEZIONI) {
                    if (l.getEmailIstruttore().equalsIgnoreCase(emailIstruttore) &&
                            l.getFasciaOraria().equalsIgnoreCase(ora) &&
                            l.getTipoLezione().equalsIgnoreCase("privata")) {

                        return true; // Lo slot è occupato da un'altra lezione privata confermata/in corso
                    }
                }
            }
        }
        return false;
    }

}
