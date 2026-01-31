package InMemory;
import Dao.LezioneDao;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;

import java.util.ArrayList;
import java.util.List;
public class LezioneDaoInMemory implements LezioneDao {
    private static final List<LezioneModel> lezioni = new ArrayList<>();

    public LezioneDaoInMemory()  {
        // Esempio di lezioni pre-caricate
        LezioneModel l1 = new LezioneModel();
        l1.setEmailIstruttore("coach1@test.com");
        l1.setNomeIstruttore("Luigi");
        l1.setCognomeIstruttore("Verdi");
        l1.setFasciaOraria("09-11");
        l1.setLivello("Principiante");
        l1.setTariffa((float)22.00);
        l1.setTipoLezione("Privata");
        l1.setGiorniDisponibili("Lun,Mer,Ven");

        lezioni.add(l1);

        LezioneModel l2 = new LezioneModel();
        l2.setEmailIstruttore("coach2@test.com");
        l2.setNomeIstruttore("Sara");
        l2.setCognomeIstruttore("Bianchi");
        l2.setFasciaOraria("17-19");
        l2.setLivello("Agonista");
        l2.setTariffa((float)40.0);
        l2.setTipoLezione("Gruppo");
        l2.setGiorniDisponibili("Mar,Gio");

        lezioni.add(l2);
    }

    public List<LezioneModel> cercaLezione(LezioneModel filtro) {
        List<LezioneModel> risultati = new ArrayList<>();
        for (LezioneModel l : lezioni) {
            boolean match = true;
            if (filtro.getTipoLezione() != null && !filtro.getTipoLezione().isEmpty()) {
                match &= l.getTipoLezione().equalsIgnoreCase(filtro.getTipoLezione());
            }
            if (filtro.getLivello() != null && !filtro.getLivello().isEmpty()) {
                match &= l.getLivello().equalsIgnoreCase(filtro.getLivello());
            }
            if (match) {
                risultati.add(l);
            }
        }
        return risultati;
    }
    public boolean controllaEmail(String nome, String cognome, String email) throws UtentenonpresenteException {

        boolean esiste = lezioni.stream().anyMatch(l ->
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

