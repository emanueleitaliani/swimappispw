package Controller;

import Dao.UserDao;
import Bean.Utenteloggatobean;
import Bean.CredenzialiBean;
import Model.UtenteloggatoModel;
import Model.CredenzialiModel;
import Exceptions.CredenzialisbagliateException;
import Exceptions.UtentenonpresenteException;
import Other.FactoryDao;


public class Logincontroller {


    // Il metodo deve semplicemente lanciare le eccezioni senza catturarle qui dentro
    public Utenteloggatobean login(CredenzialiBean credenzialiBean) throws CredenzialisbagliateException, UtentenonpresenteException {
        CredenzialiModel credenzialiModel = new CredenzialiModel(
                credenzialiBean.getEmail(),
                credenzialiBean.getPassword()
        );

        // Rimosso il try-catch interno per le tue eccezioni personalizzate
        UserDao userDAO = FactoryDao.getUserDAO();
        UtenteloggatoModel utenteloggatoModel = userDAO.loginMethod(credenzialiModel);

        if (utenteloggatoModel != null) {
            String nome = utenteloggatoModel.getNome();
            String cognome = utenteloggatoModel.getCognome();
            boolean ruolo = utenteloggatoModel.isIstructor();

            return new Utenteloggatobean(credenzialiBean, nome, cognome, ruolo);
        }

        return null;
    }
}

