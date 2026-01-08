package Gui;

import Bean.Prenotazionebean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Other.StatoPrenotazione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestisciPrenotazioniIstruttoreGui {

    private Utenteloggatobean utente;
    private final Prenotazionecontroller controller;

    // Elementi collegati a SceneBuilder
    @FXML private TableView<Prenotazionebean> tblPrenotazioni;
    @FXML private TableColumn<Prenotazionebean, String> colUtente;
    @FXML private TableColumn<Prenotazionebean, String> colGiorno;
    @FXML private TableColumn<Prenotazionebean, Float> colOra;
    @FXML private TableColumn<Prenotazionebean, String> colInfo;
    @FXML private Button tornaindietro;
    @FXML private Button accetta;
    @FXML private Button rifiuta;
    @FXML private Label lblEsito; // Label per stampare l'esito delle azioni

    public GestisciPrenotazioniIstruttoreGui() {
        this.controller = new Prenotazionecontroller();
    }

    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
        caricaPrenotazioni();
    }

    @FXML
    public void initialize() {
        // Mappatura delle colonne con i campi del Prenotazionebean
        colUtente.setCellValueFactory(new PropertyValueFactory<>("emailUser"));
        colGiorno.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        colOra.setCellValueFactory(new PropertyValueFactory<>("hour"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));

        // Messaggio di default se la tabella è vuota
        tblPrenotazioni.setPlaceholder(new Label("Nessuna prenotazione in attesa di approvazione."));
    }

    private void caricaPrenotazioni() {
        List<Prenotazionebean> lista = controller.getPrenotazioniIstruttore(utente.getCredenziali().getEmail());
        if (lista != null) {
            ObservableList<Prenotazionebean> data = FXCollections.observableArrayList(lista);
            tblPrenotazioni.setItems(data);
        }
    }

    @FXML
    private void accettaPrenotazione(ActionEvent event) {
        // 1. Recupero la riga selezionata
        Prenotazionebean selezionata = tblPrenotazioni.getSelectionModel().getSelectedItem();

        if (selezionata != null) {
            // 2. Eseguo l'aggiornamento tramite controller
            controller.aggiornaStatoPrenotazione(selezionata.getIdPrenotazione(), StatoPrenotazione.ACCETTATA);

            // 3. Stampo l'esito nella label
            lblEsito.setText("✅ Prenotazione di " + selezionata.getEmailUser() + " ACCETTATA");
            lblEsito.setStyle("-fx-text-fill: #27ae60;"); // Colore verde

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operazione Riuscita");
            alert.setHeaderText(null);
            alert.setContentText("Hai accettato con successo la prenotazione di: " + selezionata.getEmailUser());
            alert.showAndWait();

            // 4. Refresh della tabella
            caricaPrenotazioni();
        } else {
            mostraAvvisoSelezione();
        }
    }

    @FXML
    private void rifiutaPrenotazione(ActionEvent event) {
        Prenotazionebean selezionata = tblPrenotazioni.getSelectionModel().getSelectedItem();

        if (selezionata != null) {
            controller.aggiornaStatoPrenotazione(selezionata.getIdPrenotazione(), StatoPrenotazione.RIFIUTATA);

            // Stampo l'esito nella label
            lblEsito.setText("❌ Prenotazione di " + selezionata.getEmailUser() + " RIFIUTATA");
            lblEsito.setStyle("-fx-text-fill: #c0392b;");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Prenotazione Rifiutata");
            alert.setHeaderText(null);
            alert.setContentText("La prenotazione di " + selezionata.getEmailUser() + " è stata scartata.");
            alert.showAndWait();

            caricaPrenotazioni();
        } else {
            mostraAvvisoSelezione();
        }
    }

    @FXML
    private void tornaIndietro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/homeistruttore.fxml"));
            loader.setControllerFactory(c -> new HomeIstruttoreGui(utente));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblEsito.setText("Errore nel caricamento della Home.");
        }
    }

    private void mostraAvvisoSelezione() {
        lblEsito.setText("⚠️ Seleziona prima una riga dalla tabella!");
        lblEsito.setStyle("-fx-text-fill: #f39c12;"); // Colore arancione
    }
}

