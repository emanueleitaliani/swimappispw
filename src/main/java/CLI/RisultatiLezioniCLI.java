package CLI;

import Bean.LezioneBean;
import Bean.Utenteloggatobean;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;

import java.util.List;
import java.util.Scanner;
import Other.Stampa;
public class RisultatiLezioniCLI extends AbstractState {
    Utenteloggatobean user;
    private LezioneBean lezioneInfoBean;

    private List<LezioneBean> risultatiBean;

    Scanner scanner = new Scanner(System.in);

    public RisultatiLezioniCLI(Utenteloggatobean user, List<LezioneBean> risultatiBean, LezioneBean lezioneInfoBean) {
        this.user = user;
        this.risultatiBean = risultatiBean;
        this.lezioneInfoBean = lezioneInfoBean;
    }

    @Override
    public void entry(StateMachineImpl context) {
        stampaBenvenuto();
        action(context);
    }

    @Override
    public void action(StateMachineImpl context) {
        if (risultatiBean == null || risultatiBean.isEmpty()) {
            Stampa.println("Nessuna lezione trovata con i filtri inseriti.");
            Stampa.println("Tornando al menu principale...");
            goNext(context, new CercaLezioneCLI(user));
            return;
        }

        Stampa.println("Lezioni trovate:");
        int i = 1;
        for (LezioneBean lezione : risultatiBean) {
            Stampa.println(i + ") " + lezione.getTipoLezione() + " - " +
                    lezione.getLivello() + " - " +
                    lezione.getFasciaOraria() + " - €" + lezione.getTariffa());
            i++;
        }

        Stampa.println("\nSeleziona il numero della lezione per vedere i dettagli, oppure digita 0 per tornare al menu:");
        int scelta = leggiNumero(risultatiBean.size());

        if (scelta == 0) {
            goNext(context, new CercaLezioneCLI(user));
            return;
        }

        LezioneBean selezionata = risultatiBean.get(scelta - 1);
        stampaDettagliLezione(selezionata);

        Stampa.println("\nVuoi prenotare questa lezione?");
        Stampa.println("1) Sì, voglio prenotare");
        Stampa.println("2) Torna alla lista");

        int sceltaPrenotazione = leggiNumero(2);

        if (sceltaPrenotazione == 1) {
            goNext(context, new RichiediPrenotazioneCLI(user, selezionata));
        } else {
            goNext(context, new RisultatiLezioniCLI(user, risultatiBean, lezioneInfoBean));
        }
    }

    private void stampaDettagliLezione(LezioneBean lezione) {
        Stampa.printlnBlu("\n--- DETTAGLI LEZIONE ---");
        Stampa.println("Istruttore: " + lezione.getNomeIstruttore() + " " + lezione.getCognomeIstruttore());
        Stampa.println("Tipo: " + lezione.getTipoLezione());
        Stampa.println("Livello: " + lezione.getLivello());
        Stampa.println("Giorno: " + lezione.getGiorni());
        Stampa.println("Fascia oraria: " + lezione.getFasciaOraria());
        Stampa.println("Prezzo: €" + lezione.getTariffa());
        Stampa.println("Note: " + (lezione.getNoteAggiuntive() == null ? "Nessuna" : lezione.getNoteAggiuntive()));
    }

    private int leggiNumero(int max) {
        int scelta = -1;
        while (scelta < 0 || scelta > max) {
            try {
                String input = scanner.nextLine();
                scelta = Integer.parseInt(input);
                if (scelta < 0 || scelta > max) {
                    Stampa.errorPrint("Inserisci un numero valido tra 0 e " + max);
                }
            } catch (NumberFormatException e) {
                Stampa.errorPrint("Input non valido, inserisci un numero.");
            }
        }
        return scelta;
    }

    @Override
    public void stampaBenvenuto() {
        Stampa.println(" ");
        Stampa.printBlu("Home Utente -> ");
        Stampa.printBlu("PrenotaLezione -> ");
        Stampa.printlnBlu("Risultati");
        Stampa.printlnBlu("--------------- RISULTATI RICERCA LEZIONI -------------");
    }
}

