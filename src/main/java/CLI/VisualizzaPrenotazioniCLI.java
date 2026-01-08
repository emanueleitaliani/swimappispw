package CLI;
import java.util.List;

import Bean.Prenotazionebean;
import Controller.Prenotazionecontroller;
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;
import Bean.Utenteloggatobean;
public class VisualizzaPrenotazioniCLI extends AbstractState {

    private final Utenteloggatobean user;

    public VisualizzaPrenotazioniCLI(Utenteloggatobean user) {
        this.user = user;
    }

    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        action(context);
    }

    @Override
    public void action(StateMachineImpl context) {
        try {
            Prenotazionecontroller controller = new Prenotazionecontroller();
            List<Prenotazionebean> prenotazioni = controller.getPrenotazioniByEmail(user.getCredenziali().getEmail());

            if (prenotazioni.isEmpty()) {
                Stampa.println("🔎 Nessuna prenotazione trovata per il tuo account.");
            } else {
                Stampa.println("📋 Le tue prenotazioni:");
                for (Prenotazionebean p : prenotazioni) {
                    Stampa.println("-----------------------------");
                    Stampa.println("📌 ID Prenotazione: " + p.getIdPrenotazione());

                    // --- AGGIUNTA VISUALIZZAZIONE STATO ---
                    String statoTesto = p.getStatus().toString();
                    if (p.getStatus().toString().equals("ACCETTATA")) {
                        Stampa.println("✅ STATO: " + statoTesto);
                    } else if (p.getStatus().toString().equals("RIFIUTATA")) {
                        Stampa.println("❌ STATO: " + statoTesto);
                    } else {
                        Stampa.println("⏳ STATO: " + statoTesto + " (In attesa)");
                    }
                    // ---------------------------------------

                    Stampa.println("📅 Giorno: " + p.getGiorno());
                    Stampa.println("🕒 Ora: " + p.getHour());
                    Stampa.println("👨‍🏫 Istruttore: " + p.getNome() + " " + p.getCognome());
                    Stampa.println("💰 Prezzo: " + p.getPrezzo() + "€");
                    Stampa.println("📧 Email Istruttore: " + p.getEmailIstruttore());
                    Stampa.println("ℹ️ Info: " + p.getInfo());
                    Stampa.println("📧 Email Utente: " + p.getEmailUser());
                }
            }

        } catch (Exception e) {
            Stampa.errorPrint("❌ Errore durante la visualizzazione: " + e.getMessage());
        }

        Stampa.println("\nPremi INVIO per tornare indietro...");
        new java.util.Scanner(System.in).nextLine();
        goBack(context);
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println("🔍 Visualizzazione Prenotazioni Attive");
    }
}