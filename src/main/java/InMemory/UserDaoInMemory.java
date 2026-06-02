package InMemory;

import Dao.UserDao;
import Exceptions.CredenzialisbagliateException;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;
import Other.Stampa;
import java.util.logging.Level;
import java.util.logging.Logger;
public class UserDaoInMemory implements UserDao {

        private static final Logger logger = Logger.getLogger(UserDaoInMemory.class.getName());

        @Override
        public UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel)
                throws UtentenonpresenteException, CredenzialisbagliateException {

            String email = credenzialiModel.getEmail();
            String password = credenzialiModel.getPassword();

            Stampa.print("EMAIL CERCATA: [" + email + "]");
            Stampa.print("UTENTI DISPONIBILI: " + LocalDatabase.Utenti.keySet());

            if (!LocalDatabase.Utenti.containsKey(email)) {
                throw new UtentenonpresenteException();
            }

            UtenteloggatoModel utente = LocalDatabase.Utenti.get(email);

            if (!utente.getCredenziali().getPassword().equals(password)) {
                throw new CredenzialisbagliateException();
            }

            return utente;
        }

        @Override
        public void registrazioneMethod(UtenteloggatoModel registrazioneModel) {
            String email = registrazioneModel.getCredenziali().getEmail();
            LocalDatabase.Utenti.put(email, registrazioneModel);
            logger.log(Level.INFO, "Utente registrato correttamente in LocalDatabase: {0}", email);
        }

        @Override
        public void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException {
            if (LocalDatabase.Utenti.containsKey(registrazioneModel.getCredenziali().getEmail())) {
                throw new EmailgiainusoException();
            }
        }

        @Override
        public void registraIstruttoreMethod(String email, String nome, String cognome) {
            if (!LocalDatabase.Utenti.containsKey(email)) {
                logger.log(Level.SEVERE, "Utente non trovato per diventare istruttore: {0}", email);
                return;
            }

            UtenteloggatoModel utente = LocalDatabase.Utenti.get(email);
            utente.setNome(nome);
            utente.setCognome(cognome);
            utente.setIstructor(true);

            logger.log(Level.INFO, "Utente promosso a istruttore in LocalDatabase: {0}", email);
        }
    }
