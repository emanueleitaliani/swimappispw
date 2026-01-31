package Gui;

import Bean.LezioneBean;
import Bean.Prenotazionebean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.UtentenonpresenteException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class ConfermaPrenotazioneGui extends HomeUtenteGui {


    private LezioneBean lezione;

    // UNICI CAMPI PRESENTI NELL’FXML
    @FXML private TextField txtOra;
    @FXML private TextField txtEmailUtente;

    @FXML private Button btnConferma;
    @FXML private Button btnIndietro;

    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
    }

    public void setLezione(LezioneBean l) {
        this.lezione = l;
    }

    private boolean validaEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    @FXML
    private void confermaPrenotazione() {

        // ⚠️ CONTROLLO ORA
        float ora;
        try {
            ora = Float.parseFloat(txtOra.getText().trim());
        } catch (NumberFormatException e) {
            mostraAlert(Alert.AlertType.ERROR, "Ora non valida!", "Inserisci un valore numerico (es: 14.30)");
            return;
        }

        // ⚠️ CONTROLLO EMAIL UTENTE
        String emailUtente = txtEmailUtente.getText().trim();

        if (!validaEmail(emailUtente)) {
            mostraAlert(Alert.AlertType.ERROR, "Email non valida!", "Inserisci una email corretta.");
            return;
        }

        try {
            // CREA BEAN PRENOTAZIONE
            Prenotazionebean pren = new Prenotazionebean();

            pren.setIdPrenotazione(ThreadLocalRandom.current().nextInt(0, 100));

            // ❗Tutti questi dati arrivano dalla LEZIONE
            pren.setGiorno(lezione.getGiorni());
            pren.setHour(ora);
            pren.setPrezzo(lezione.getTariffa());
            pren.setInfo(lezione.getNoteAggiuntive());

            pren.setNome(lezione.getNomeIstruttore());
            pren.setCognome(lezione.getCognomeIstruttore());
            pren.setEmailIstruttore(lezione.getEmailIstruttore());

            pren.setEmailUser(emailUtente);

            // CONTROLLER
            Prenotazionecontroller controller = new Prenotazionecontroller();

            controller.controllaEmail(
                    pren.getNome(),
                    pren.getCognome(),
                    pren.getEmailIstruttore()
            );

            controller.richiediprenotazione(pren);

            mostraAlert(Alert.AlertType.INFORMATION, "Prenotazione inviata!", "Registrata con successo.");
        } catch (LezioneGiaPrenotataException e) {
            mostraAlert(Alert.AlertType.ERROR, TITOLO_ERRORE, e.getMessage());


        } catch (UtentenonpresenteException | SQLException ex) {
            mostraAlert(Alert.AlertType.ERROR, TITOLO_ERRORE, ex.getMessage());
        }
    }
    @FXML
    private void tornaACercaLezione() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/cercalezionereal.fxml"));

            // passo l'utente alla nuova GUI
            loader.setControllerFactory(c -> new CercaLezioneGui(utente));

            Parent root = loader.load();

            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            mostraAlert(Alert.AlertType.ERROR, TITOLO_ERRORE, "Impossibile tornare alla ricerca lezioni");
        }
    }
}

