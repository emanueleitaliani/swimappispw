package CLI;

import Bean.Prenotazionebean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Other.Stampa;
import Other.StatoPrenotazione;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;
import java.util.List;
import java.util.Scanner;

public class GestisciPrenotazioniIstruttoreCLI extends AbstractState {
    private final Utenteloggatobean user;
    private final Prenotazionecontroller controller;

    public GestisciPrenotazioniIstruttoreCLI(Utenteloggatobean user) {
        this.user = user;
        this.controller = new Prenotazionecontroller();
    }

    @Override
    public void action(StateMachineImpl context) {
        Scanner scan = new Scanner(System.in);

        // Recupero le prenotazioni in stato INCORSO per questo istruttore
        List<Prenotazionebean> lista = controller.getPrenotazioniIstruttore(user.getCredenziali().getEmail());

        if (lista == null || lista.isEmpty()) {
            Stampa.println("Non ci sono prenotazioni in attesa di approvazione.");
            goNext(context, new IstructorCLI(user));
            return;
        }

        Stampa.println("Seleziona una prenotazione per cambiare lo stato:");
        for (int i = 0; i < lista.size(); i++) {
            Prenotazionebean p = lista.get(i);
            Stampa.println((i + 1) + ") Utente: " + p.getEmailUser() + " | Giorno: " + p.getGiorno() + " | Info: " + p.getInfo());
        }

        Stampa.print("\nScelta (0 per tornare indietro): ");
        int scelta;

        try {
            scelta = scan.nextInt();
        } catch (Exception e) {
            Stampa.errorPrint("Inserisci un numero valido.");
            scan.nextLine(); // Pulisce il buffer
            return;
        }

        if (scelta == 0) {
            goNext(context, new IstructorCLI(user));
            return;
        }

        if (scelta > 0 && scelta <= lista.size()) {
            Prenotazionebean selezionata = lista.get(scelta - 1);
            gestisciSingolaPrenotazione(selezionata);
        } else {
            Stampa.errorPrint("Scelta fuori intervallo.");
        }

        goNext(context, new IstructorCLI(user));
    }

    private void gestisciSingolaPrenotazione(Prenotazionebean p) {
        Scanner scan = new Scanner(System.in);
        Stampa.println("\n--- Gestione Prenotazione ID: " + p.getIdPrenotazione() + " ---");
        Stampa.println("Dettagli: " + p.getInfo() + " - Ore: " + p.getHour());
        Stampa.println("1. Accetta");
        Stampa.println("2. Rifiuta");
        Stampa.print("Inserisci azione: ");

        int azione;
        try {
            azione = scan.nextInt();
        } catch (Exception e) {
            Stampa.errorPrint("Azione non valida.");
            return;
        }

        // Ora usiamo StatoPrenotazione invece di Prenotazionebean.Status
        if (azione == 1) {
            p.setStatus(StatoPrenotazione.ACCETTATA);
            controller.aggiornaStatoPrenotazione(p.getIdPrenotazione(), StatoPrenotazione.ACCETTATA);
            Stampa.println(" Prenotazione accettata con successo!");
        } else if (azione == 2) {
            p.setStatus(StatoPrenotazione.RIFIUTATA);
            controller.aggiornaStatoPrenotazione(p.getIdPrenotazione(), StatoPrenotazione.RIFIUTATA);
            Stampa.println(" Prenotazione rifiutata.");
        } else {
            Stampa.println("Operazione annullata.");
        }
    }

    @Override
    public void entry(StateMachineImpl cli) {
        stampaBenvenuto();
        mostraSchermata();
    }

    @Override
    public void mostraSchermata() {
        Stampa.printlnBlu("-------------- GESTIONE PRENOTAZIONI --------------");
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println("Istruttore: " + user.getNome());
    }
}