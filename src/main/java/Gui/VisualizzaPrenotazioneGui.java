package Gui;

import Bean.Prenotazionebean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Other.Stampa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VisualizzaPrenotazioneGui extends HomeUtenteGui{

    private Utenteloggatobean utente;

    public VisualizzaPrenotazioneGui(){}
    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
        caricaPrenotazioni();
    }
    @FXML
    private Pane visualizzaPane;

    @FXML
    private TableView<Prenotazionebean> tablePrenotazioni;

    @FXML private TableColumn<Prenotazionebean, Integer> ID;
    @FXML private TableColumn<Prenotazionebean, String> GIORNO;
    @FXML private TableColumn<Prenotazionebean, Float> ORA;
    @FXML private TableColumn<Prenotazionebean, String> NOME;
    @FXML private TableColumn<Prenotazionebean, String> COGNOME;
    @FXML private TableColumn<Prenotazionebean, Float> PREZZO;
    @FXML private TableColumn<Prenotazionebean, String> EMAIL;
    @FXML private TableColumn<Prenotazionebean, String> STATO;




    @FXML
    public void initialize() {
        ID.setCellValueFactory(new PropertyValueFactory<>("idPrenotazione"));
        GIORNO.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        ORA.setCellValueFactory(new PropertyValueFactory<>("hour"));
        NOME.setCellValueFactory(new PropertyValueFactory<>("nome"));
        COGNOME.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        PREZZO.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        EMAIL.setCellValueFactory(new PropertyValueFactory<>("emailIstruttore"));
        STATO.setCellValueFactory(new PropertyValueFactory<>("status"));


    }

    private void caricaPrenotazioni() {
        try {
            Prenotazionecontroller controller = new Prenotazionecontroller();

            List<Prenotazionebean> lista =
                    controller.getPrenotazioniByEmail(utente.getCredenziali().getEmail());

            ObservableList<Prenotazionebean> dati =
                    FXCollections.observableArrayList(lista);

            tablePrenotazioni.setItems(dati);

        } catch (Exception e) {
            Stampa.errorPrint("Errore caricamento prenotazioni: " + e.getMessage());
        }
    }

    @FXML
    private void tornaIndietro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/gestisciprenotazioni.fxml"));
            Parent root = loader.load();

            GestionePrenotazioniGui controller = loader.getController();
            controller.setUtente(utente);

            Stage stage = (Stage) visualizzaPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Stampa.println("Errore ritorno indietro: " + e.getMessage());
        }
    }
}

