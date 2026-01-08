package CLI;

import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Controller.Registrazionecontroller;
import Exceptions.EmailgiainusoException;
import Exceptions.EmailnonvalidaException;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;
import Other.Stampa;
import java.util.Scanner;
import java.util.regex.Pattern;
public class RegistrazioneCLI extends AbstractState {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void action(StateMachineImpl context) {
        Scanner scanner = new Scanner(System.in);
        Stampa.println("--- REGISTRAZIONE ---");

        Stampa.println("Inserisci il tuo nome: ");
        String nome = scanner.nextLine();

        Stampa.println("Inserisci il tuo cognome: ");
        String cognome = scanner.nextLine();

        Stampa.println("Inserisci la tua email: ");
        String email = scanner.nextLine();
        try{
            isValidEmail(email);
        } catch(EmailnonvalidaException e){
            Stampa.errorPrint("Email non valida");
            return;
        }




        Stampa.println("Inserisci la tua password: ");
        String password = scanner.nextLine();

        Stampa.println("Sei un istruttore? (s/n): ");
        boolean isIstructor = scanner.nextLine().equalsIgnoreCase("s");


        CredenzialiBean credenziali = new CredenzialiBean(email,password);


        // Utilizza il costruttore esistente di Utenteloggatobean
        Utenteloggatobean utente = new Utenteloggatobean(credenziali, nome, cognome, isIstructor);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.getCredenziali().setEmail(email);
        utente.getCredenziali().setPassword(password);
        utente.setRuolo(isIstructor);

        Registrazionecontroller registrazionecontroller= new Registrazionecontroller();
        try {
            registrazionecontroller.registrazione(utente);
        } catch (EmailgiainusoException e) {
            Stampa.errorPrint("❌ Email già in uso.");
            return; // interrompe l'esecuzione della registrazione
        }

        // Aggiungi l'utente al context
        context.setUtenteloggatobean(utente);

        Stampa.println("✅ Registrazione completata!");

        if (isIstructor) {
            goNext(context,new IstructorCLI(utente)); // esempio nome stato HomeIstruttore
        } else {
            goNext(context,new UserCLI(utente)); // esempio nome stato HomeUtente
        }
    }
    private boolean isValidEmail(String email) throws EmailnonvalidaException{
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }



    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        mostraSchermata();
    }
    @Override
    public void exit(StateMachineImpl context){
        Stampa.println("riportato alla home");
    }
    @Override
    public void stampaBenvenuto() {
        Stampa.println("Benvenuto inserisci le credenziali per registrarti");
    }



    public void mostraSchermata() {
        Stampa.println("1. Conferma registrazione"); //in questo caso facciamo il login in automatico
        Stampa.println("2. Torna indietro");
        Stampa.println("0. Esci");
        Stampa.print("Opzione scelta: ");

    }

}
