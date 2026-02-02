package CLI;

import Bean.LezioneBean;
import Controller.Prenotazionecontroller;
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





    public void inseriscivalori( Scanner scanner) {
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

        prenotazionebean.setGiorno(giorno);
        prenotazionebean.setInfo(info);
        prenotazionebean.setPrezzo(prezzo);
        prenotazionebean.setNome(nomeIstruttore);
        prenotazionebean.setCognome(cognomeIstruttore);
        prenotazionebean.setEmailIstruttore(emailIstruttore);
        prenotazionebean.setEmailUser(utente.getCredenziali().getEmail());

        try {
            Stampa.print(" Inserisci l'orario (es: 14.30): ");
            float oraInput = Float.parseFloat(scanner.nextLine());
            prenotazionebean.setHour(oraInput);

            Prenotazionecontroller prenotazionecontroller = new Prenotazionecontroller();

            // 1. Controllo email (facoltativo se già fatto nel controller)
            prenotazionecontroller.controllaEmail(nomeIstruttore, cognomeIstruttore, emailIstruttore);

            // 2. TENTATIVO DI PRENOTAZIONE (Qui gestiamo l'eccezione specifica)
            prenotazionecontroller.richiediprenotazione(prenotazionebean);

            // Se arriviamo qui, la prenotazione è andata a buon fine
            Stampa.println("\n Richiesta di prenotazione inviata con successo!");
            Stampa.println("Giorno: " + giorno + " alle ore " + oraInput);
            Stampa.println(" Istruttore: " + nomeIstruttore + " " + cognomeIstruttore);

        } catch (NumberFormatException e) {
            Stampa.errorPrint("❌ Errore: Inserisci un orario valido (es. 15.00).");
        } catch (UtentenonpresenteException e) {
            Stampa.errorPrint("❌ Errore: L'istruttore selezionato non è più disponibile.");
        } catch (LezioneGiaPrenotataException e) {
            // --- QUESTA È LA GESTIONE CHE TI MANCAVA ---
            Stampa.errorPrint("\n⚠️ ATTENZIONE: " + e.getMessage());
            Stampa.println("Non puoi prenotare due volte la stessa lezione.");
        } catch (SQLException e) {
            Stampa.errorPrint("❌ Errore Database: " + e.getMessage());
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

