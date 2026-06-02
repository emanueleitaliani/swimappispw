package InMemory;
import Dao.LezioneDao;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;

import java.util.ArrayList;
import java.util.List;
public class LezioneDaoInMemory implements LezioneDao {


    public LezioneDaoInMemory()  {
    }

    @Override
    public List<LezioneModel> cercaLezione(LezioneModel filtro) {
        List<LezioneModel> risultati = new ArrayList<>();

        for (LezioneModel l : LocalDatabase.LEZIONI) {
            boolean match = true;

            // 1. Filtro Tipo Lezione
            if (filtro.getTipoLezione() != null && !filtro.getTipoLezione().isEmpty()) {
                match &= l.getTipoLezione().equalsIgnoreCase(filtro.getTipoLezione());
            }

            // 2. Filtro Livello
            if (filtro.getLivello() != null && !filtro.getLivello().isEmpty()) {
                match &= l.getLivello().equalsIgnoreCase(filtro.getLivello());
            }

            // 3. GESTIONE GENERICA DEI GIORNI MULTIPLI (Nuovo)
            if (filtro.getGiorniDisponibili() != null && !filtro.getGiorniDisponibili().isEmpty()) {
                // Separa la stringa del filtro (es: "Lunedì, Mercoledì" -> ["Lunedì", " Mercoledì"])
                String[] giorniCercati = filtro.getGiorniDisponibili().split(",");
                boolean giornoTrovato = false;

                for (String g : giorniCercati) {
                    String giornoPulito = g.trim().toLowerCase(); // Rimuove spazi e mette in minuscolo

                    // Controlla se il giorno della lezione in memoria (es: "martedì" o "Lun,Mer,Ven")
                    // contiene il giorno che l'utente sta cercando
                    if (l.getGiorniDisponibili().toLowerCase().contains(giornoPulito)) {
                        giornoTrovato = true;
                        break; // Trovata corrispondenza per almeno uno dei giorni, usciamo dal ciclo interno
                    }
                }

                // Applica il risultato al match complessivo
                match &= giornoTrovato;
            }

            // Se passa tutti i filtri attivi, aggiungiamo la lezione ai risultati
            if (match) {
                risultati.add(l);
            }
        }
        return risultati;
    }
    @Override
    public boolean controllaEmail(String nome, String cognome, String email) throws UtentenonpresenteException {
        boolean esiste = LocalDatabase.LEZIONI.stream().anyMatch(l ->
                l.getEmailIstruttore().equalsIgnoreCase(email) &&
                        l.getNomeIstruttore().equalsIgnoreCase(nome) &&
                        l.getCognomeIstruttore().equalsIgnoreCase(cognome)
        );

        if (!esiste) {
            throw new UtentenonpresenteException();
        }

        return true;
    }

    // Metodo per aggiungere nuove lezioni

}

