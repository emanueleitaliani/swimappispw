package CLI;

import Bean.Utenteloggatobean;
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.Initialstate;
import Pattern.StateMachineImpl;
import java.util.Scanner;
import java.util.InputMismatchException;
public class UserCLI extends AbstractState {
    protected Utenteloggatobean user;

    public UserCLI(Utenteloggatobean user){
        this.user = user;
    }

    @Override
    public void action(StateMachineImpl context){


        Scanner scan = new Scanner(System.in);
        int choice;

        while((choice = scan.nextInt()) != 0) {

            try{
                switch(choice){
                    case (1):
                        goNext(context, new GestisciPrenotazioneCLI(user));
                        break;
                    case (2):
                        goNext(context,new CercaLezioneCLI(user));

                        break;
                    default:
                        Stampa.errorPrint("Input invalido. Scegliere un'opzione tra quelle disponibili: ");
                        break;
                }
            } catch (InputMismatchException e){
                Stampa.errorPrint("Input non valido. Per favore, inserisci un numero intero: ");
                scan.nextLine(); // Consuma l'input non valido)
            }
        }

        goNext(context, new Initialstate());
    }

    @Override
    public void mostraSchermata(){
        // Modificato per SwimApp
        Stampa.println("   1. Gestisci Prenotazioni");
        Stampa.println("   2. Cerca Lezione");
        Stampa.println("   0. Logout");
        Stampa.print("Opzione scelta: ");
    }

    @Override
    public void stampaBenvenuto(){
        Stampa.println(" ");
        Stampa.printlnBlu("-------------- HOME STUDENTE - SWIMAPP --------------");
        Stampa.println("Ciao " + this.user.getNome() + ", scegli un'opzione:");
    }

    @Override
    public void entry(StateMachineImpl cli){
        stampaBenvenuto();
        mostraSchermata();
    }
}

