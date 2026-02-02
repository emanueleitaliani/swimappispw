package Gui;

import Bean.Utenteloggatobean;
import Other.Stampa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeIstruttoreGui {

    protected Utenteloggatobean user;
    private static final Logger logger = Logger.getLogger(HomeIstruttoreGui.class.getName());

    @FXML
    private Button btnGestisciPrenotazioni;
    @FXML
    private Button btnLogout;


    public HomeIstruttoreGui() {
    }

    // Costruttore con parametro per il controller factory
    public HomeIstruttoreGui(Utenteloggatobean user) {
        this.user = user;
    }

    @FXML
    private void gestisciPrenotazioni(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/gestisciprenistruttore.fxml"));
            Parent root = loader.load();

            // Passiamo l'utente al controller successivo
            GestisciPrenotazioniIstruttoreGui controller = loader.getController();
            controller.setUtente(user);

            cambiaScena(event, root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore nel caricamento gestione prenotazioni", e);
            mostraErrore("Errore", "Impossibile aprire la gestione prenotazioni.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Torna alla schermata di Login (InitialState della CLI)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent root = loader.load();

            cambiaScena(event, root);


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante il logout", e);
        }
    }

    // Metodo di utility per cambiare scena
    private void cambiaScena(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
