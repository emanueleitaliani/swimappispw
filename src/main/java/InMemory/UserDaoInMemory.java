package InMemory;
import Dao.UserDao;
import Exceptions.CredenzialisbagliateException;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserDaoInMemory implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoInMemory.class.getName());

    // "Database" in memoria
    private static final Map<String, UtenteloggatoModel> databaseUtenti = new HashMap<>();

    static {
        // Utente normale
        CredenzialiModel cred1 = new CredenzialiModel("user1@example.com", "password1");
        UtenteloggatoModel utente1 = new UtenteloggatoModel(cred1, "Mario", "Rossi", false);
        databaseUtenti.put(cred1.getEmail(), utente1);

        // Istruttore
        CredenzialiModel cred2 = new CredenzialiModel("istruttore@example.com", "password2");
        UtenteloggatoModel utente2 = new UtenteloggatoModel(cred2, "Luigi", "Verdi", true);
        databaseUtenti.put(cred2.getEmail(), utente2);

        CredenzialiModel cred3 = new CredenzialiModel("coach1@test.com", "password3");
        UtenteloggatoModel utente3 = new UtenteloggatoModel(cred3, "Luigi", "Verdi", true);
        databaseUtenti.put(cred3.getEmail(), utente3);

        logger.info("Database utenti prepopolato con 2 utenti di test");
    }

    @Override
    public UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel)
            throws UtentenonpresenteException, CredenzialisbagliateException {

        String email = credenzialiModel.getEmail();
        String password = credenzialiModel.getPassword();
        System.out.println("EMAIL CERCATA: [" + credenzialiModel.getEmail() + "]");
        System.out.println("UTENTI DISPONIBILI: " + databaseUtenti.keySet());
        // 1. Controlla se l'utente esiste
        if (!databaseUtenti.containsKey(email)) {
            throw new UtentenonpresenteException();
        }

        UtenteloggatoModel utente = databaseUtenti.get(email);

        // 2. Controlla password
        if (!utente.getCredenziali().getPassword().equals(password)) {
            throw new CredenzialisbagliateException();
        }

        return utente;
    }

    @Override
    public void registrazioneMethod(UtenteloggatoModel registrazioneModel) {
        String email = registrazioneModel.getCredenziali().getEmail();

        databaseUtenti.put(email, registrazioneModel);

        logger.info("Utente registrato correttamente in memoria: " + email);
    }

    public void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException {
        String email = registrazioneModel.getCredenziali().getEmail();

        if (databaseUtenti.containsKey(email)) {
            throw new EmailgiainusoException();
        }
    }

    public void registraIstruttoreMethod(String email, String nome, String cognome) {
        if (!databaseUtenti.containsKey(email)) {
            logger.severe("Utente non trovato per diventare istruttore: " + email);
            return;
        }

        UtenteloggatoModel utente = databaseUtenti.get(email);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setIstructor(true);

        logger.info("Utente promosso a istruttore: " + email);
    }


}
