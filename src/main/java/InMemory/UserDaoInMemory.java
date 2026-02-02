package InMemory;

import Dao.UserDao;
import Exceptions.CredenzialisbagliateException;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;
import Other.Stampa;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoInMemory implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoInMemory.class.getName());
    private static final Map<String, UtenteloggatoModel> databaseUtenti = new HashMap<>();

    static {
        popolaDatabaseEsempi();
        logger.info("Database utenti prepopolato con utenti di test");
    }

    private static void popolaDatabaseEsempi() {
        CredenzialiModel cred1 = new CredenzialiModel("user1@example.com", "password1");
        databaseUtenti.put(cred1.getEmail(), new UtenteloggatoModel(cred1, "Mario", "Rossi", false));

        CredenzialiModel cred2 = new CredenzialiModel("istruttore@example.com", "password2");
        databaseUtenti.put(cred2.getEmail(), new UtenteloggatoModel(cred2, "Luigi", "Verdi", true));

        CredenzialiModel cred3 = new CredenzialiModel("coach1@test.com", "password3");
        databaseUtenti.put(cred3.getEmail(), new UtenteloggatoModel(cred3, "Luigi", "Verdi", true));
    }

    @Override
    public UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel)
            throws UtentenonpresenteException, CredenzialisbagliateException {

        String email = credenzialiModel.getEmail();
        String password = credenzialiModel.getPassword();

        Stampa.print("EMAIL CERCATA: [" + email + "]");
        Stampa.print("UTENTI DISPONIBILI: " + databaseUtenti.keySet());

        if (!databaseUtenti.containsKey(email)) {
            throw new UtentenonpresenteException();
        }

        UtenteloggatoModel utente = databaseUtenti.get(email);

        if (!utente.getCredenziali().getPassword().equals(password)) {
            throw new CredenzialisbagliateException();
        }

        return utente;
    }

    @Override
    public void registrazioneMethod(UtenteloggatoModel registrazioneModel) {
        String email = registrazioneModel.getCredenziali().getEmail();
        databaseUtenti.put(email, registrazioneModel);

        // CORREZIONE: Logging con parametri (built-in formatting)
        logger.log(Level.INFO, "Utente registrato correttamente in memoria: {0}", email);
    }

    @Override
    public void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException {
        if (databaseUtenti.containsKey(registrazioneModel.getCredenziali().getEmail())) {
            throw new EmailgiainusoException();
        }
    }

    @Override
    public void registraIstruttoreMethod(String email, String nome, String cognome) {
        if (!databaseUtenti.containsKey(email)) {
            // CORREZIONE: Uso della formattazione integrata per evitare concatenazione
            logger.log(Level.SEVERE, "Utente non trovato per diventare istruttore: {0}", email);
            return;
        }

        UtenteloggatoModel utente = databaseUtenti.get(email);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setIstructor(true);

        logger.log(Level.INFO, "Utente promosso a istruttore: {0}", email);
    }
}