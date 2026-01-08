package Dao;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Exceptions.CredenzialisbagliateException;
import Model.UtenteloggatoModel;
import Model.CredenzialiModel;

// uso interfaccia perchè devo solo chiamare i metodi
public interface UserDao {
    UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel)throws UtentenonpresenteException,CredenzialisbagliateException;

    void registrazioneMethod(UtenteloggatoModel registrazioneModel);

    void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException;

    void registraIstruttoreMethod(String email, String nome, String cognome) ;

}
