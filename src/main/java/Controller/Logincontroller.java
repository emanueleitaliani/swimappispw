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

    String nome;
    String cognome;
    Boolean ruolo;
    public Utenteloggatobean login(CredenzialiBean credenzialiBean)throws CredenzialisbagliateException,UtentenonpresenteException{
        CredenzialiModel credenzialiModel = new CredenzialiModel(
                credenzialiBean.getEmail(),
                credenzialiBean.getPassword()
        );

        Utenteloggatobean utenteloggatobean = new Utenteloggatobean(credenzialiBean,nome,cognome,ruolo);


        try{

            // collegarsi al Dao per ottenere gli utenti

            UserDao userDAO = FactoryDao.getUserDAO();

            UtenteloggatoModel utenteloggatoModel = userDAO.loginMethod(credenzialiModel);
            CredenzialiModel credenzialimodel = utenteloggatoModel.getCredenziali();
            CredenzialiBean credenzialibean = new CredenzialiBean(
                    credenzialimodel.getEmail(),
                    credenzialimodel.getPassword()
            );

            if (utenteloggatoModel != null && utenteloggatoModel.getCredenziali() != null) {

                utenteloggatobean.setNome(utenteloggatoModel.getNome());
                utenteloggatobean.setCognome(utenteloggatoModel.getCognome());

                utenteloggatobean.setCredenziali(credenzialibean);
                utenteloggatobean.setRuolo(utenteloggatoModel.isIstructor());
                return utenteloggatobean;
            }else {
                Stampa.errorPrint("❌ Credenziali mancanti o errate");
                return null;
            }
            // prende dalla Dao le credenziali dell'utente


        } catch (UtentenonpresenteException un) {
            return null;
        } catch (CredenzialisbagliateException cl){
            return null;
        }

    }
}

