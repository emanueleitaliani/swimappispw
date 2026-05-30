package CLI;

import Bean.Prenotazionebean;
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
            // 1. Specifichiamo all'utente che può digitare 0 per annullare
            Stampa.print("🔢 Inserisci l'ID della prenotazione da cancellare (oppure 0 per annullare): ");
            String input = scanner.nextLine().trim();

            // 2. Controllo immediato sulla scelta di uscita
            if ("0".equals(input)) {
                Stampa.println("Annullamento in corso... Nessuna operazione effettuata.");
                goBack(context);
                return; // Interrompe il metodo ed evita il resto della logica
            }

            // 3. Se non ha digitato 0, procediamo con il parsing dell'ID reale
            int id = Integer.parseInt(input);

            // Creiamo il Bean da passare al controller
            Prenotazionebean bean = new Prenotazionebean();
            bean.setIdPrenotazione(id);
            bean.setEmailUser(utente.getCredenziali().getEmail());

            // Invocazione del controller tramite Bean
            boolean cancellata = controller.cancellaPrenotazione(bean);

            if (cancellata) {
                Stampa.println("✅ Prenotazione cancellata con successo.");
            } else {
                Stampa.println("❌ Nessuna prenotazione trovata con quell'ID per il tuo account.");
            }
            goBack(context);

        } catch (NumberFormatException e) {
            Stampa.println("ID non valido. Inserisci un numero intero.");
            goBack(context);
        } catch (Exception e) {
            Stampa.println("Errore durante la cancellazione: " + e.getMessage());
            goBack(context);
        }
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
