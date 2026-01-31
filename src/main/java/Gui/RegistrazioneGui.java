package Gui;

import Controller.Registrazionecontroller;
import Bean.CredenzialiBean;
import Bean.Utenteloggatobean;
import Exceptions.EmailgiainusoException;
import Exceptions.EmailnonvalidaException;
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
    private Button Indietro;

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
        String userNome = nome.getText();
        String userCognome = cognome.getText();
        String userEmail = email.getText();
        String userPassword = password.getText();
        boolean isIstruttore = isIstructor.isSelected();


        // Controllo campi
        if (userNome.isBlank() || userCognome.isBlank() || userEmail.isBlank() || userPassword.isBlank()) {
            campiError.setText("⚠️ Tutti i campi sono obbligatori.");
            return;
        }

        // Validazione email
        try {
            validateEmail(userEmail);
        } catch (EmailnonvalidaException e) {
            campiError.setText("❌ Email non valida.");
            return;
        }

        // Bean e controller logico
        CredenzialiBean credenziali = new CredenzialiBean(userEmail,userPassword);
        credenziali.setEmail(userEmail);
        credenziali.setPassword(userPassword);
        this.utente = new Utenteloggatobean(credenziali, userNome, userCognome, isIstruttore);

        Registrazionecontroller controller = new Registrazionecontroller();
        try {
            controller.registrazione(utente);
            campiError.setText(""); // pulizia errori
            loadConferma(); // pagina successiva
        } catch (EmailgiainusoException e) {
            campiError.setText("❌ Email già in uso.");
        }
    }

    private void validateEmail(String email) throws Exceptions.EmailnonvalidaException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailnonvalidaException();
        }
    }

    @FXML
    public void loadConferma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/homeutente.fxml"));
            loader.setControllerFactory(c -> new HomeUtenteGui(this.utente));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) nome.getScene().getWindow(); // prende lo stage corrente
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della conferma: " + e.getMessage());
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
            System.err.println("Errore nel caricamento del login: " + e.getMessage());
        }
    }
}
