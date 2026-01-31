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
        int choice = -1;

        while (choice != 0) {
            mostraSchermata();

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consuma newline

                switch (choice) {
                    case 0:
                        Stampa.println(" Uscita dal menu gestione prenotazioni.");
                        break;

                    case 1:
                        // Vai alla schermata che mostra le prenotazioni dell'utente
                        goNext(context, new VisualizzaPrenotazioniCLI(user));
                        break;

                    case 2:
                        // Vai alla schermata che permette la cancellazione
                        goNext(context, new cancellaPrenotazioneCLI(user));
                        break;

                    default:
                        Stampa.errorPrint(" Scelta non valida. Seleziona un'opzione tra quelle elencate.");
                        break;
                }

            } catch (InputMismatchException e) {
                Stampa.errorPrint(" Input non valido. Inserisci un numero.");
                scanner.nextLine(); // Pulizia del buffer
            }
        }

        goBack(context); // Torna indietro al termine
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


