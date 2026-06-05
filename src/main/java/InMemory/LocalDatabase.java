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
    protected static Map<String, UtenteloggatoModel> Utenti = new HashMap<>();
    protected static final List<LezioneModel> Lezioni = new ArrayList<>();
    protected static  List<PrenotazioneModel> Prenotazioni = new ArrayList<>();

    static {
        popolaDatabaseEsempi();
        logger.info("LocalDatabase inizializzato e prepopolato con dati stabili.");
    }

    private static void popolaDatabaseEsempi() {
        // --- POPOLAMENTO UTENTI ---
        CredenzialiModel cred1 = new CredenzialiModel("user1@example.com", "password1");
        Utenti.put(cred1.getEmail(), new UtenteloggatoModel(cred1, "Mario", "Rossi", false));

        CredenzialiModel cred2 = new CredenzialiModel("istruttore@example.com", "password2");
        Utenti.put(cred2.getEmail(), new UtenteloggatoModel(cred2, "Luigi", "Verdi", false));

        CredenzialiModel cred3 = new CredenzialiModel("coach1@test.com", "password3");
        Utenti.put(cred3.getEmail(), new UtenteloggatoModel(cred3, "Luigi", "Masini", true));

        CredenzialiModel cred4 = new CredenzialiModel("coach2@test.com", "password4");
        Utenti.put(cred4.getEmail(), new UtenteloggatoModel(cred4, "Sara", "Bianchi", true));

        // --- POPOLAMENTO LEZIONI (Rifattorizzato per eliminare i letterali duplicati) ---

        // Lezione 1: Coach 1
        LezioneModel l1 = new LezioneModel();
        String emailCoach1 = "coach1@test.com";
        UtenteloggatoModel coach1 = Utenti.get(emailCoach1);

        if (coach1 != null) {
            l1.setEmailIstruttore(emailCoach1);
            l1.setNomeIstruttore(coach1.getNome());       // Assegna "Luigi" dinamicamente
            l1.setCognomeIstruttore(coach1.getCognome()); // Assegna "Masini" dinamicamente
        }
        l1.setFasciaOraria("09-11");
        l1.setLivello("Principiante");
        l1.setTariffa(22.00f);
        l1.setTipoLezione("Privata");
        l1.setGiorniDisponibili("lunedì, mercoledì, venerdì"); // Formato minuscolo compatibile con i filtri
        Lezioni.add(l1);

        // Lezione 2: Coach 2
        LezioneModel l2 = new LezioneModel();
        String emailCoach2 = "coach2@test.com";
        UtenteloggatoModel coach2 = Utenti.get(emailCoach2);

        if (coach2 != null) {
            l2.setEmailIstruttore(emailCoach2);
            l2.setNomeIstruttore(coach2.getNome());       // Assegna "Sara" dinamicamente
            l2.setCognomeIstruttore(coach2.getCognome()); // Assegna "Bianchi" dinamicamente
        }
        l2.setFasciaOraria("17-19");
        l2.setLivello("Agonista");
        l2.setTariffa(40.0f);
        l2.setTipoLezione("In Gruppo");
        l2.setGiorniDisponibili("martedì, giovedì"); // Formato minuscolo compatibile con i filtri
        Lezioni.add(l2);
    }

    private LocalDatabase() {
        // Costruttore privato per prevenire istanziazioni
    }
}