package CLI;

import Bean.Utenteloggatobean;
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.Initialstate;
import Pattern.StateMachineImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class IstructorCLI extends AbstractState {

    protected Utenteloggatobean user;

    public IstructorCLI(Utenteloggatobean user) {
        this.user = user;
    }

    @Override
    public void action(StateMachineImpl context) {
        Scanner scan = new Scanner(System.in);
        int choice;


        while (true) {
            try {
                choice = scan.nextInt();

                if (choice == 0) {
                    break; // logout
                }

                if (choice == 1) {
                    goNext(context, new GestisciPrenotazioniIstruttoreCLI(user));
                    return; // passa allo stato successivo
                } else {
                    Stampa.errorPrint("Input invalido. Scegliere un'opzione valida.");
                    mostraSchermata();
                }

            } catch (InputMismatchException e) {
                Stampa.errorPrint("Errore: Inserisci un numero intero.");
                scan.nextLine(); // pulisce buffer
                mostraSchermata();
            }
        }

        // Logout → torna allo stato iniziale
        goNext(context, new Initialstate());
    }

    @Override
    public void mostraSchermata() {
        Stampa.println("   1. Gestisci Prenotazioni Ricevute");
        Stampa.println("   0. Logout");
        Stampa.print("Opzione scelta: ");
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println(" ");
        Stampa.printlnBlu("-------------- HOME ISTRUTTORE - SWIMAPP --------------");
        Stampa.println("Bentornato Coach " + this.user.getNome() + "!");
    }

    @Override
    public void entry(StateMachineImpl cli) {
        stampaBenvenuto();
        mostraSchermata();
    }
}
