package Gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Controller.Logincontroller;
import Exceptions.CredenzialisbagliateException;
import Exceptions.UtentenonpresenteException;

import java.net.URL;
import java.util.logging.Logger;
import java.io.IOException;

public class LoginGui {
    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label credenzialiError;

    @FXML
    private Label credenzialiSbagliate;

    protected Utenteloggatobean utenteloggatobean;
    private static final Logger logger = Logger.getLogger(LoginGui.class.getName());

    @FXML
    void login() {
        String userEmail = this.emailField.getText().trim();
        String userPassword = this.passwordField.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            mostraAlert("Errore Input", "Campi obbligatori", "Per favore inserisci sia email che password.");
            return;
        }

        try {
            CredenzialiBean credenzialiBean = new CredenzialiBean(userEmail, userPassword);
            Logincontroller loginController = new Logincontroller();
            this.utenteloggatobean = loginController.login(credenzialiBean);

            caricaHome(utenteloggatobean.getRuolo());

        } catch (CredenzialisbagliateException e) {
            mostraAlert("Login Fallito", "Password Errata", "La password inserita non è corretta.");
        } catch (UtentenonpresenteException u) {
            mostraAlert("Login Fallito", "Utente Inesistente", "Non è stato trovato alcun utente con questa email.");
        } catch (Exception e) {
            mostraAlert("Errore Sistema", "Errore imprevisto", e.getMessage());
        }
    }

    // Metodo helper per creare l'Alert a schermo
    private void mostraAlert(String titolo, String header, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    // AGGIUNTO MouseEvent per risolvere l'errore nel FXML
    @FXML
    public void caricaRegistrazione(MouseEvent event) {
        try {
            URL fxmlUrl = LoginGui.class.getResource("/Fxml/registrazione.fxml");
            if (fxmlUrl == null) {
                logger.severe("File registrazione.fxml non trovato!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setControllerFactory(c -> new RegistrazioneGui(this.utenteloggatobean));
            Parent parent = loader.load();
            cambiaScena(parent);
        } catch (IOException e) {
            logger.severe("Errore nel caricamento della registrazione: " + e.getMessage());
        }
    }

    public void caricaHome(boolean isIstructor) {
        try {
            String path = isIstructor ? "/Fxml/homeistruttore.fxml" : "/Fxml/homeutente.fxml";
            FXMLLoader loader = new FXMLLoader(LoginGui.class.getResource(path));

            loader.setControllerFactory(c -> isIstructor ?
                    new HomeIstruttoreGui(utenteloggatobean) :
                    new HomeUtenteGui(utenteloggatobean));

            Parent parent = loader.load();
            cambiaScena(parent);
        } catch (IOException e) {
            logger.severe("Errore in caricaHome: " + e.getMessage());
        }
    }

    private void cambiaScena(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}