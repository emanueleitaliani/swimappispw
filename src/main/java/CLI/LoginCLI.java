package CLI;

import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Controller.Logincontroller;
import Exceptions.CredenzialisbagliateException;
import Exceptions.UtentenonpresenteException;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;
import Other.Stampa;
import java.util.Scanner;

public class LoginCLI extends AbstractState {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void entry(StateMachineImpl context){
        stampaBenvenuto();
        mostraSchermata();
    }

    @Override
    public void stampaBenvenuto(){
        Stampa.println(" ");
        Stampa.printlnBlu("-------------- LOGIN SWIMAPP --------------");
    }

    @Override
    public void mostraSchermata(){
        Stampa.println("Benvenuto effettua il login ");
    }

    @Override
    public void action(StateMachineImpl context) {

        Logincontroller logincontroller = new Logincontroller();


        Stampa.println("Inserisci email: ");
        String email = scanner.nextLine();

        Stampa.println("Inserisci password: ");
        String password = scanner.nextLine();

        try {

            CredenzialiBean credenzialiBean = new CredenzialiBean(email,password);

            Utenteloggatobean utente = logincontroller.login(credenzialiBean);

            if (utente != null) {

                Stampa.println("\nLogin effettuato con successo!");
                Stampa.println("Benvenuto/a, " + utente.getNome() + " " + utente.getCognome());
                Stampa.println("Email: " + utente.getCredenziali().getEmail());
                Stampa.println("Ruolo: " + (utente.isIstructor() ? "Istruttore" : "Utente"));

                AbstractState homeCLI;

                if (utente.isIstructor()) {
                    homeCLI = new IstructorCLI(utente);
                } else {
                    homeCLI = new UserCLI(utente);
                }

                goNext(context, homeCLI);
            }

        } catch (UtentenonpresenteException | CredenzialisbagliateException e) {

            Stampa.errorPrint("Errore durante il login: " + e.getMessage());
            mostraSchermata(); // ripropone login
        }
    }
}
