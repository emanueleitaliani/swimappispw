package Gui;

import Bean.Utenteloggatobean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Logger;

public class HomeUtenteGui {


    @FXML
    private Pane homeUtente;
    protected static final String TITOLO_ERRORE = "Errore";


    @FXML
    private Button logoutButton;

    protected Utenteloggatobean utente;

    // Costruttore personalizzato per ricevere il bean
    protected HomeUtenteGui(){}
    public HomeUtenteGui(Utenteloggatobean utente) {
        this.utente = utente;
    }
    private static final Logger logger = Logger.getLogger(HomeUtenteGui.class.getName());

    @FXML
    private void gotoCercaLezione() {
        try {
            // Creo il loader con il path dell'FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/cercalezionereal.fxml"));

            // Imposto il controller con il costruttore che prende il bean Utente
            loader.setControllerFactory(c -> new CercaLezioneGui(utente));

            // Carico il parent dalla scena
            Parent root = loader.load();

            // Recupero lo stage dalla scena corrente
            Stage stage = (Stage) homeUtente.getScene().getWindow();

            // Imposto la nuova scena
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            logger.severe("Errore critico nel caricamento di cercalezionereal.fxml: " + e.getMessage());
        }
    }
    @FXML
    private void gotoGestisciPrenotazioni() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Fxml/gestisciprenotazioni.fxml")
            );

            // Crea il controller passando l'utente nel costruttore
            loader.setControllerFactory(c -> new GestionePrenotazioniGui(utente));

            Parent root = loader.load();

            Stage stage = (Stage) homeUtente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception e) {
            logger.severe("Errore critico nella gestione delle prenotazioni (gestisciprenotazioni.fxml): " + e.getMessage());
        }
    }


    @FXML
    private void gohandleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeUtente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.severe("Errore critico durante l'esecuzione del logout: " + e.getMessage());
        }
    }
    protected void mostraAlert(Alert.AlertType tipo, String titolo, String messaggio) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
