package Controller;

import Dao.UserDao;
import Bean.Utenteloggatobean;
import Bean.CredenzialiBean;
import Model.UtenteloggatoModel;
import Model.CredenzialiModel;
import Exceptions.CredenzialisbagliateException;
import Exceptions.UtentenonpresenteException;
import Other.FactoryDao;
import Other.Stampa;

public class Logincontroller {


    public Utenteloggatobean login(CredenzialiBean credenzialiBean)throws CredenzialisbagliateException,UtentenonpresenteException{
        // 1. Converti Bean in Model per il DAO
        CredenzialiModel credenzialiModel = new CredenzialiModel(
                credenzialiBean.getEmail(),
                credenzialiBean.getPassword()
        );

        try {
            // 2. Ottieni i dati dal DB tramite il DAO
            UserDao userDAO = FactoryDao.getUserDAO();
            UtenteloggatoModel utenteloggatoModel = userDAO.loginMethod(credenzialiModel);

            if (utenteloggatoModel != null) {
                // 3. ORA che hai i dati, crea il Bean correttamente
                // Recuperiamo i dati dal Model restituito dal DB
                String nome = utenteloggatoModel.getNome();
                String cognome = utenteloggatoModel.getCognome();
                boolean ruolo = utenteloggatoModel.isIstructor();

                // 4. Crea il bean con i dati REALI
                Utenteloggatobean utenteloggatobean = new Utenteloggatobean(credenzialiBean, nome, cognome, ruolo);

                return utenteloggatobean;
            } else {
                Stampa.errorPrint("❌ Credenziali mancanti o errate");
                return null;
            }


        } catch (UtentenonpresenteException |CredenzialisbagliateException cl) {
            return null;
        }

    }
}

