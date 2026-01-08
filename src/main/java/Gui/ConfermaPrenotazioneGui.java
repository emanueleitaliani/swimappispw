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

    private Utenteloggatobean utente;
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
        // ❗QUI NON COMPILO NULLA perché i dati non arrivano da TextField
        // L'FXML serve SOLO per ora e emailUtente
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ora non valida!");
            alert.setContentText("Inserisci un valore numerico per l'ora, esempio: 14.30");
            alert.showAndWait();
            return;
        }

        // ⚠️ CONTROLLO EMAIL UTENTE
        String emailUtente = txtEmailUtente.getText().trim();

        if (!validaEmail(emailUtente)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Email non valida!");
            alert.setContentText("Inserisci una email corretta, esempio: nome@gmail.com");
            alert.showAndWait();
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Prenotazione inviata!");
            alert.setContentText("La tua prenotazione è stata registrata con successo.");
            alert.showAndWait();
        } catch (LezioneGiaPrenotataException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText(e.getMessage());
            alert.showAndWait();    // GESTIONE DUPLICATO


        } catch (UtentenonpresenteException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("Impossibile tornare alla ricerca lezioni");
            alert.showAndWait();
        }
    }
}

