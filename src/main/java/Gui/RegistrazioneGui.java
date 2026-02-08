package Gui;

import Controller.Registrazionecontroller;
import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Exceptions.EmailgiainusoException;
import Exceptions.EmailnonvalidaException;
import Other.Stampa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegistrazioneGui {

    @FXML
    private TextField nome;

    @FXML
    private TextField cognome;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private CheckBox isIstructor;

    @FXML
    private Button confermaRegistrazione;

    @FXML
    private Button indietro;

    @FXML
    private Label campiError;

    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    protected Utenteloggatobean utente;

    public RegistrazioneGui(Utenteloggatobean utente) {
        this.utente = utente;
    }

    @FXML
    void handleRegistrazione() {

        String userNome = nome.getText().trim();
        String userCognome = cognome.getText().trim();
        String userEmail = email.getText().trim();
        String userPassword = password.getText().trim();
        boolean isIstruttore = isIstructor.isSelected(); // Recupero il valore della checkbox

        if (userNome.isBlank() || userCognome.isBlank() || userEmail.isBlank() || userPassword.isBlank()) {
            campiError.setText("⚠️ Tutti i campi sono obbligatori.");
            return;
        }

        try {
            validateEmail(userEmail);

            // Creazione Bean
            CredenzialiBean credenziali = new CredenzialiBean(userEmail, userPassword);
            this.utente = new Utenteloggatobean(credenziali, userNome, userCognome, isIstruttore);

            // Chiamata al controller logico
            Registrazionecontroller controller = new Registrazionecontroller();
            controller.registrazione(utente);

            campiError.setText("");

            // CHIAMATA AL METODO DI CAMBIO INTERFACCIA DINAMICO
            caricaInterfacciaDopoRegistrazione(isIstruttore);

        } catch (EmailnonvalidaException e) {
            campiError.setText("❌ Email non valida.");
        } catch (EmailgiainusoException e) {
            campiError.setText("❌ Email già in uso.");
        }
    }

    /**
     * Metodo dinamico per cambiare interfaccia in base al ruolo
     */
    private void caricaInterfacciaDopoRegistrazione(boolean istruttore) {
        try {
            // 1. Scegliamo il file FXML in base al ruolo
            String fxmlPath = istruttore ? "/Fxml/homeistruttore.fxml" : "/Fxml/homeutente.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // 2. Iniettiamo l'utente nel controller della nuova interfaccia
            loader.setControllerFactory(c -> {
                if (istruttore) {
                    return new HomeIstruttoreGui(this.utente);
                } else {
                    return new HomeUtenteGui(this.utente);
                }
            });

            Parent root = loader.load();
            Stage stage = (Stage) nome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Stampa.println("Errore nel caricamento dell'interfaccia post-registrazione: " + e.getMessage());

        }
    }




    private void validateEmail(String email) throws Exceptions.EmailnonvalidaException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailnonvalidaException();
        }
    }


    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(RegistrazioneGui.class.getResource("/Fxml/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) nome.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Stampa.println("Errore nel caricamento del login: " + e.getMessage());
        }
    }
}
