package InMemory;
import Dao.LezioneDao;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;

import java.util.ArrayList;
import java.util.List;
public class LezioneDaoInMemory implements LezioneDao {


    public LezioneDaoInMemory()  {
        //costruttore versione in memory
    }

    @Override
    public List<LezioneModel> cercaLezione(LezioneModel filtro) {
        List<LezioneModel> risultati = new ArrayList<>();

        for (LezioneModel l : LocalDatabase.Lezioni) {
            boolean match = true;

            // 1. Filtro Tipo Lezione
            if (filtro.getTipoLezione() != null && !filtro.getTipoLezione().isEmpty()) {
                match &= l.getTipoLezione().equalsIgnoreCase(filtro.getTipoLezione());
            }

            // 2. Filtro Livello
            if (filtro.getLivello() != null && !filtro.getLivello().isEmpty()) {
                match &= l.getLivello().equalsIgnoreCase(filtro.getLivello());
            }

            // 3. GESTIONE GENERICA DEI GIORNI MULTIPLI (Logica complessa estratta)
            if (filtro.getGiorniDisponibili() != null && !filtro.getGiorniDisponibili().isEmpty()) {
                match &= verificaGiorniDisponibili(l.getGiorniDisponibili(), filtro.getGiorniDisponibili());
            }

            // Se passa tutti i filtri attivi, aggiungiamo la lezione ai risultati
            if (match) {
                risultati.add(l);
            }
        }
        return risultati;
    }

    // METODO DI SUPPORTO: Estrae il ciclo innestato abbattendo la Complessità Cognitiva
    private boolean verificaGiorniDisponibili(String giorniLezione, String giorniCercatiFiltro) {
        // Separa la stringa del filtro (es: "Lunedì, Mercoledì" -> ["Lunedì", " Mercoledì"])
        String[] giorniCercati = giorniCercatiFiltro.split(",");
        String giorniLezioneLower = giorniLezione.toLowerCase();

        for (String g : giorniCercati) {
            String giornoPulito = g.trim().toLowerCase(); // Rimuove spazi e mette in minuscolo

            // Controlla se i giorni della lezione contengono il giorno cercato
            if (giorniLezioneLower.contains(giornoPulito)) {
                return true; // Trovata corrispondenza, usciamo subito restituendo true
            }
        }
        return false; // Nessun giorno del filtro corrisponde a quelli della lezione
    }
    @Override
    public boolean controllaEmail(String nome, String cognome, String email) throws UtentenonpresenteException {
        boolean esiste = LocalDatabase.Lezioni.stream().anyMatch(l ->
                l.getEmailIstruttore().equalsIgnoreCase(email) &&
                        l.getNomeIstruttore().equalsIgnoreCase(nome) &&
                        l.getCognomeIstruttore().equalsIgnoreCase(cognome)
        );

        if (!esiste) {
            throw new UtentenonpresenteException();
        }

        return true;
    }


}

