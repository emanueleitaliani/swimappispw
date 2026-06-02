package CLI;

import Bean.LezioneBean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioneGiaOccupataException;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.UtentenonpresenteException;
import Other.Stampa;
import Pattern.AbstractState;
import Pattern.StateMachineImpl;
import java.util.Scanner;
import Bean.Utenteloggatobean;
import Bean.Prenotazionebean;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.SQLException;
public class RichiediPrenotazioneCLI extends AbstractState {
    Utenteloggatobean utente;
    private final LezioneBean lezioneSelezionata;

    public RichiediPrenotazioneCLI(Utenteloggatobean utente,LezioneBean lezioneSelezionata){
        this.utente = utente;
        this.lezioneSelezionata = lezioneSelezionata;
    }
    @Override
    public void action(StateMachineImpl context) {
        Scanner scanner = new Scanner(System.in);
        int scelta=-1;




        while (scelta!=0) {
            mostraSchermata();
            try {
                Stampa.print("Scegli un'opzione: ");
                String input = scanner.nextLine();
                scelta = Integer.parseInt(input);

                Stampa.println("DEBUG: input letto -> " + input);

                switch (scelta){

                    case 1:
                        Stampa.println("Hai scelto di prenotare la Lezione");

                        // Rimosso il try-catch qui perché Inseriscivalori gestisce già tutto
                        inseriscivalori(scanner);
                        break;

                        case 0:
                        Stampa.println("DEBUG: caso 0 scelto, ritorno");
                        goBack(context);
                        return;
                    default:
                        Stampa.println("❌ Scelta non valida.");
                        break;
                }
            } catch (NumberFormatException e) {
                Stampa.println("Input non valido. Inserisci un numero intero.");
            }
        }

        // Se necessario tornare alla schermata iniziale
        Stampa.println("DEBUG: uscita dal ciclo while");
        goBack(context);
    }





    public void inseriscivalori(Scanner scanner) {
        Prenotazionebean prenotazionebean = new Prenotazionebean();
        int idRandom = ThreadLocalRandom.current().nextInt(0, 100);
        prenotazionebean.setIdPrenotazione(idRandom);

        // Dati recuperati dalla lezione selezionata
        String giorno = lezioneSelezionata.getGiorni();
        String info = lezioneSelezionata.getNoteAggiuntive();
        float prezzo = lezioneSelezionata.getTariffa();
        String nomeIstruttore = lezioneSelezionata.getNomeIstruttore();
        String cognomeIstruttore = lezioneSelezionata.getCognomeIstruttore();
        String emailIstruttore = lezioneSelezionata.getEmailIstruttore();

        // 🎯 RECUPERIAMO LA FASCIA ORARIA DIRETTAMENTE DALLA LEZIONE COME STRINGA
        String fasciaOraria = lezioneSelezionata.getFasciaOraria();

        prenotazionebean.setGiorno(giorno);
        prenotazionebean.setInfo(info);
        prenotazionebean.setPrezzo(prezzo);
        prenotazionebean.setNome(nomeIstruttore);
        prenotazionebean.setCognome(cognomeIstruttore);
        prenotazionebean.setEmailIstruttore(emailIstruttore);
        prenotazionebean.setEmailUser(utente.getCredenziali().getEmail());

        // 🎯 IMPOSTIAMO LA STRINGA DELLA FASCIA ORARIA NEL BEAN AGGIORNATO
        prenotazionebean.setHour(fasciaOraria);

        try {
            // Non chiediamo più l'orario all'utente, lo mostriamo e basta per conferma!
            Stampa.println("\nFascia oraria della lezione selezionata: " + fasciaOraria);
            Stampa.print("Confermare la prenotazione? (Premi INVIO per continuare, scrivi 'no' per annullare): ");
            String conferma = scanner.nextLine().trim();

            if (conferma.equalsIgnoreCase("no")) {
                Stampa.println("Prenotazione Annullata");
                return;
            }

            Prenotazionecontroller prenotazionecontroller = new Prenotazionecontroller();

            // 1. Controllo email
            prenotazionecontroller.controllaEmail(nomeIstruttore, cognomeIstruttore, emailIstruttore);

            // 2. TENTATIVO DI PRENOTAZIONE (Utilizza la logica a stringhe aggiornata)
            prenotazionecontroller.richiediprenotazione(prenotazionebean);

            // Se arriviamo qui, la prenotazione è andata a buon fine
            Stampa.println("\n Richiesta di prenotazione inviata con successo!");
            Stampa.println("Giorno: " + giorno + " nella fascia: " + fasciaOraria);
            Stampa.println(" Istruttore: " + nomeIstruttore + " " + cognomeIstruttore);

        } catch (UtentenonpresenteException e) {
            Stampa.errorPrint(" Errore: L'istruttore selezionato non è più disponibile.");
        } catch (LezioneGiaPrenotataException e) {
            Stampa.errorPrint("\n ATTENZIONE: " + e.getMessage());
        } catch (LezioneGiaOccupataException e) {
            Stampa.errorPrint("\n ERRORE (Slot occupato): " + e.getMessage());
        } catch (SQLException e) {
            Stampa.errorPrint(" Errore Database: " + e.getMessage());
        }
    }

    @Override
    public void entry(StateMachineImpl context){
        stampaBenvenuto();
        action(context);
    }

    @Override
    public void exit(StateMachineImpl context){
        Stampa.println("riportato alla home");
    }


    @Override
    public void stampaBenvenuto() {
        Stampa.println(" --- Benvenuto in prenota lezione di nuoto ---");
        Stampa.println("Ciao " + this.utente.getNome() + ", scegli un'opzione:");
    }


    @Override
    public void mostraSchermata() {
        Stampa.println("1. Prenota lezione");
        Stampa.println("0. Torna indietro");
        Stampa.println("opzione scelta");

    }




}

