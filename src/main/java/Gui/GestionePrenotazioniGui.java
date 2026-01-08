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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionePrenotazioniGui {

    private Utenteloggatobean utente;
    public GestionePrenotazioniGui() {
    }

    // 2️⃣ COSTRUTTORE CON PARAMETRO (puoi tenerlo se ti serve)
    public GestionePrenotazioniGui(Utenteloggatobean utente) {
        this.utente = utente;
    }

    @FXML private Button btnVisualizzaPrenotazioni;
    @FXML private Button btnCancellaPrenotazione;
    @FXML private Button btnIndietro;
    @FXML private Pane gestionePrenotazioniPane;


    // Verrà chiamato dal loader


    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
    }

    /* =============================
       VISUALIZZA PRENOTAZIONI
       ============================= */
    @FXML
    private void visualizzaPrenotazioni(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/visualizzaprenotazione.fxml"));
            Parent root = loader.load();

            // Passo l’utente alla nuova GUI
            VisualizzaPrenotazioneGui controller = loader.getController();
            controller.setUtente(utente);

            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire Visualizza Prenotazioni");
            alert.showAndWait();
        }
    }

    /* =============================
       CANCELLA PRENOTAZIONE
       ============================= */
    @FXML
    private void cancellaPrenotazione(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/cancellaprenotazione.fxml"));
            Parent root = loader.load();

            CancellaPrenotazioneGui controller = loader.getController();
            controller.setUtente(utente);

            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore apertura schermata");
            alert.setContentText("Impossibile aprire Cancella Prenotazione");
            alert.showAndWait();
        }
    }
    @FXML
    private void tornaIndietro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/homeutente.fxml"));
            loader.setControllerFactory(c -> new HomeUtenteGui(utente));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();



        } catch (IOException e) {
            Stampa.println("Errore nel ritorno alla Home: " + e.getMessage());
        }
    }

}
