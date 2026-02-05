package CLI;

import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;

import java.util.Scanner;

public class cancellaPrenotazioneCLI extends AbstractState {
    private final Utenteloggatobean utente;

    public cancellaPrenotazioneCLI(Utenteloggatobean utente) {
        this.utente = utente;
    }

    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        action(context);
    }

    @Override
    public void action(StateMachineImpl context) {
        Scanner scanner = new Scanner(System.in);
        Prenotazionecontroller controller = new Prenotazionecontroller();

        try {
            Stampa.print("🔢 Inserisci l'ID della prenotazione da cancellare: ");
            int idPrenotazione = Integer.parseInt(scanner.nextLine());



            boolean cancellata = controller.cancellaPrenotazioneById(idPrenotazione,utente.getCredenziali().getEmail());

            if (cancellata) {
                Stampa.println(" Prenotazione cancellata con successo.");
            } else {
                Stampa.println(" Nessuna prenotazione trovata con quell'ID per il tuo account.");
            }
        } catch (NumberFormatException e) {
            Stampa.println("ID non valido. Inserisci un numero intero.");
        } catch (Exception e) {
            Stampa.println("Errore durante la cancellazione: " + e.getMessage());
        }

        goBack(context);
    }

    @Override
    public void exit(StateMachineImpl context) {
        Stampa.println("👋 Uscita dalla schermata di cancellazione.");
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println(" --- Cancellazione Prenotazione ---");
        Stampa.println("Ciao " + utente.getNome() + ", inserisci l'ID della prenotazione da rimuovere.");
    }

    @Override
    public void mostraSchermata() {
        // Vuoto: non necessario in questa CLI
    }
}
