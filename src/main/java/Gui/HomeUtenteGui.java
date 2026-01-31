package Gui;

import Bean.Utenteloggatobean;
import Other.Stampa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HomeUtenteGui {


    @FXML
    private Pane Homeutente;

    @FXML
    private Button logoutButton;

    protected Utenteloggatobean utente;

    // Costruttore personalizzato per ricevere il bean
    protected HomeUtenteGui(){}
    public HomeUtenteGui(Utenteloggatobean utente) {
        this.utente = utente;
    }

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
            Stage stage = (Stage) Homeutente.getScene().getWindow();

            // Imposto la nuova scena
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("Errore nel caricamento di CercaLezione.fxml: " + e.getMessage());
            e.printStackTrace();
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

            Stage stage = (Stage) Homeutente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            Stampa.println("Gestisci prenotazioni cliccato!");

        } catch (Exception e) {
            e.printStackTrace();
            Stampa.println("Errore nella gestione delle prenotazioni: " + e.getMessage());
        }
    }


    @FXML
    private void gohandleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Homeutente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Stampa.println("Errore nel logout: " + e.getMessage());
        }
    }
}
