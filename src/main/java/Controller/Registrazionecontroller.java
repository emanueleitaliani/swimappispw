package Controller;
import Dao.UserDao;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;
import Bean.Utenteloggatobean;
import Exceptions.EmailgiainusoException;
import Other.FactoryDao;
import Other.Stampa;

public class Registrazionecontroller {

    public void registrazione(Utenteloggatobean utente) throws EmailgiainusoException {
        UtenteloggatoModel user = new UtenteloggatoModel();
        CredenzialiModel credenziali = new CredenzialiModel();
        credenziali.setEmail(utente.getCredenziali().getEmail());
        credenziali.setPassword(utente.getCredenziali().getPassword());

        user.setCredenziali(credenziali);
        user.setNome(utente.getNome());
        user.setCognome(utente.getCognome());
        user.setIstructor(utente.getRuolo());

        try{
            UserDao registrazioneDao = FactoryDao.getUserDAO();
            registrazioneDao.controllaEmailMethod(user);
            registrazioneDao.registrazioneMethod(user);

            //
            if (utente.getRuolo()){
                registrazioneDao.registraIstruttoreMethod(user.getCredenziali().getEmail(), user.getNome(), user.getCognome());
            }

        } catch (EmailgiainusoException e) {
            throw new EmailgiainusoException();
            //il controller applicativo si limita a propagarla al controller grafico
        }
    }
}

