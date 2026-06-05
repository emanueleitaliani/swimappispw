package CLI;

import Other.Stampa;
import Pattern.AbstractState;
import java.util.InputMismatchException;
import java.util.Scanner;
import Bean.Utenteloggatobean;
import Pattern.StateMachineImpl;

public class GestisciPrenotazioneCLI extends AbstractState {

    private final Utenteloggatobean user;

    public GestisciPrenotazioneCLI(Utenteloggatobean user) {
        this.user = user;
    }

    @Override
    public void action(StateMachineImpl context) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean inMenu = true; // 🌟 Flag per controllare il ciclo rimanendo fedeli ai break

        while (inMenu) {
            mostraSchermata();

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consuma newline

                switch (choice) {
                    case 0:
                        Stampa.println(" Uscita dal menu gestione prenotazioni.");
                        goBack(context);
                        inMenu = false;
                        break;

                    case 1:
                        goNext(context, new VisualizzaPrenotazioniCLI(user));
                        inMenu = false;
                        break;

                    case 2:
                        goNext(context, new cancellaPrenotazioneCLI(user));
                        inMenu = false;
                        break;

                    default:
                        Stampa.errorPrint(" Scelta non valida. Seleziona un'opzione tra quelle elencate.");
                        break; // Questo break normale ti fa rimanere nel menu
                }

            } catch (InputMismatchException e) {
                Stampa.errorPrint(" Input non valido. Inserisci un numero.");
                scanner.nextLine(); // Pulizia del buffer
                context.transition(this); // Riavvia lo stato in modo pulito
                inMenu = false;  // Spegne il ciclo corrente per far partire quello nuovo
            }
        }
    }



    @Override
    public void mostraSchermata() {
        Stampa.println("\n Gestione Prenotazioni:");
        Stampa.println("   1. Visualizza Prenotazioni");
        Stampa.println("   2. Cancella Prenotazione");
        Stampa.println("   0. Torna Indietro");
        Stampa.print("Scegli un'opzione: ");
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println(" ");
        Stampa.printlnBlu(" Home Studente -> Gestione Prenotazioni:");
    }

    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        action(context);
    }
}


