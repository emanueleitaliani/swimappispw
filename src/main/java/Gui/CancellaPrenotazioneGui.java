package Gui;

import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Other.Stampa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class CancellaPrenotazioneGui extends HomeUtenteGui{

    private  Utenteloggatobean utente;

    @FXML
    private Pane cancellaPane;

    @FXML
    private TextField txtIdPrenotazione;

    @FXML
    private Button btnConferma;

    @FXML
    private Button btnIndietro;




    public CancellaPrenotazioneGui() {}

    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
    }

    @FXML
    private void cancellaPrenotazione() {

        int id;

        try {
            id = Integer.parseInt(txtIdPrenotazione.getText());

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ID non valido!");
            alert.setContentText("Inserisci un numero valido.");
            alert.showAndWait();
            return;
        }

        try {
            Prenotazionecontroller controller = new Prenotazionecontroller();

            boolean cancellata = controller.cancellaPrenotazioneById(
                    id,
                    utente.getCredenziali().getEmail()
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            if (cancellata) {
                alert.setHeaderText("Successo");
                alert.setContentText("✅ Prenotazione cancellata!");
            } else {
                alert.setHeaderText("Non trovata");
                alert.setContentText("❌ Nessuna prenotazione con questo ID per il tuo account.");
            }

            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("❌ Errore durante la cancellazione: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void tornaIndietro() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/gestisciprenotazioni.fxml"));
            Parent root = loader.load();

            GestionePrenotazioniGui controller = loader.getController();
            controller.setUtente(utente);

            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Stampa.errorPrint("Errore ritorno a gestione: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

