package InMemory;

import Model.CredenzialiModel;
import Model.LezioneModel;
import Model.PrenotazioneModel;
import Model.UtenteloggatoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LocalDatabase {

    private static final Logger logger = Logger.getLogger(LocalDatabase.class.getName());

    // Le tabelle del nostro Database in memoria
    protected static Map<String, UtenteloggatoModel> UTENTI = new HashMap<>();
    protected static final List<LezioneModel> LEZIONI = new ArrayList<>();
    protected static  List<PrenotazioneModel> PRENOTAZIONI = new ArrayList<>();

    static {
        popolaDatabaseEsempi();
        logger.info("LocalDatabase inizializzato e prepopolato con dati stabili.");
    }

    private static void popolaDatabaseEsempi() {
        // --- POPOLAMENTO UTENTI ---
        CredenzialiModel cred1 = new CredenzialiModel("user1@example.com", "password1");
        UTENTI.put(cred1.getEmail(), new UtenteloggatoModel(cred1, "Mario", "Rossi", false));

        CredenzialiModel cred2 = new CredenzialiModel("istruttore@example.com", "password2");
        UTENTI.put(cred2.getEmail(), new UtenteloggatoModel(cred2, "Luigi", "Verdi", true));

        CredenzialiModel cred3 = new CredenzialiModel("coach1@test.com", "password3");
        UTENTI.put(cred3.getEmail(), new UtenteloggatoModel(cred3, "Luigi", "Masini", true));

        CredenzialiModel cred4 = new CredenzialiModel("coach2@test.com", "password4");
        UTENTI.put(cred4.getEmail(), new UtenteloggatoModel(cred4, "Sara", "Bianchi", true));

        // --- POPOLAMENTO LEZIONI ---
        LezioneModel l1 = new LezioneModel();
        l1.setEmailIstruttore("coach1@test.com");
        l1.setNomeIstruttore("Luigi");
        l1.setCognomeIstruttore("Masini");
        l1.setFasciaOraria("09-11");
        l1.setLivello("Principiante");
        l1.setTariffa(22.00f);
        l1.setTipoLezione("Privata");
        l1.setGiorniDisponibili("Lun,Mer,Ven");
        LEZIONI.add(l1);

        LezioneModel l2 = new LezioneModel();
        l2.setEmailIstruttore("coach2@test.com");
        l2.setNomeIstruttore("Sara");
        l2.setCognomeIstruttore("Bianchi");
        l2.setFasciaOraria("17-19");
        l2.setLivello("Agonista");
        l2.setTariffa(40.0f);
        l2.setTipoLezione("Gruppo");
        l2.setGiorniDisponibili("Mar,Gio");
        LEZIONI.add(l2);
    }

    private LocalDatabase() {
        // Costruttore privato per prevenire istanziazioni
    }
}