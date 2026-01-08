package CLI;

import java.util.*;
import Bean.CredenzialiBean;
import Bean.LezioneBean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioniNonTrovateException; // Import fondamentale
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;

public class CercaLezioneCLI extends AbstractState {

    private final Utenteloggatobean user;
    private LezioneBean lezioneInfoBean;
    private List<LezioneBean> risultatiBean = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public CercaLezioneCLI(Utenteloggatobean user) {
        this.user = user;
    }

    @Override
    public void action(StateMachineImpl context) {
        boolean ricercaInCorso = true;

        while (ricercaInCorso) {
            try {
                // Chiamiamo il metodo che ora può lanciare l'eccezione
                risultatiBean = cercalezione();

                // Se arriviamo qui, significa che sono stati trovati risultati
                Stampa.println("\nOttimo! Sono state trovate " + risultatiBean.size() + " lezioni.");
                ricercaInCorso = false;
                goNext(context, new RisultatiLezioniCLI(user, risultatiBean, lezioneInfoBean));

            } catch (LezioniNonTrovateException e) {
                // Gestione dell'eccezione: mostriamo il messaggio d'errore del controller
                Stampa.errorPrint("\n--- " + e.getMessage() + " ---");
                Stampa.println("Cosa desideri fare?");
                Stampa.println("1. Modifica i filtri e riprova");
                Stampa.println("0. Torna alla Home");
                Stampa.print("Scelta: ");

                String scelta = scanner.nextLine();
                if (scelta.equals("0")) {
                    ricercaInCorso = false;
                    goNext(context, new UserCLI(user));
                }
                // Se preme 1, il ciclo while ricomincia e richiama cercalezione()
            }
        }
    }

    // Aggiungiamo 'throws' perché questo metodo chiama il controller
    public List<LezioneBean> cercalezione() throws LezioniNonTrovateException {
        Stampa.println("\n--- Inserimento Filtri (Digita 'exit' in qualsiasi momento per annullare) ---");

        Stampa.print("Inserisci il tipo di lezione (privata, in gruppo): ");
        String tipo = scanner.nextLine();
        if (tipo.equalsIgnoreCase("exit")) {
            // Se l'utente vuole uscire, lanciamo comunque l'eccezione o torniamo una lista vuota gestita
            throw new LezioniNonTrovateException("Ricerca annullata dall'utente.");
        }

        Stampa.print("Inserisci il livello (principiante, intermedio, agonista): ");
        String livello = scanner.nextLine();

        Stampa.print("Inserisci la fascia oraria (es. 10-12, 14-16): ");
        String fasciaOraria = scanner.nextLine();

        Stampa.print("Inserisci il giorno (es. lunedi, martedi): ");
        String giorni = scanner.nextLine().replace("'", "").toLowerCase();

        Stampa.print("Inserisci note aggiuntive o premi Invio: ");
        String noteAggiuntive = scanner.nextLine();

        Float prezzoMassimo;
        Stampa.print("Prezzo massimo: ");
        try {
            String inputPrezzo = scanner.nextLine();
            prezzoMassimo = inputPrezzo.isEmpty() ? 0.0f : Float.parseFloat(inputPrezzo);
        } catch (NumberFormatException e) {
            prezzoMassimo = 0.0f;
        }

        // Preparazione del Bean
        CredenzialiBean credenzialiFittizie = new CredenzialiBean("");
        Utenteloggatobean istruttoreVuoto = new Utenteloggatobean(credenzialiFittizie, "", "");
        lezioneInfoBean = new LezioneBean(tipo, giorni, prezzoMassimo, istruttoreVuoto, livello, fasciaOraria, noteAggiuntive);

        // Chiamata al Controller
        Prenotazionecontroller prenotazioneController = new Prenotazionecontroller();
        return prenotazioneController.ricercaLezione(lezioneInfoBean);
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println(" ");
        Stampa.printBlu("Home Utente -> ");
        Stampa.printBlu("PrenotaLezione -> ");
        Stampa.printlnBlu("Ricerca");
        Stampa.printlnBlu("--------------- CERCA LEZIONI DI NUOTO ------------------");
    }

    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        action(context);
    }
}