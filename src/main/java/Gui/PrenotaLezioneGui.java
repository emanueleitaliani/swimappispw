package Gui;

import Bean.LezioneBean;
import Bean.Utenteloggatobean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PrenotaLezioneGui extends HomeUtenteGui{

    public PrenotaLezioneGui() {
        // costruttore vuoto necessario per JavaFX
    }
    private List<LezioneBean> risultatiBean;
    private Utenteloggatobean utente;

    public void setUtente(Utenteloggatobean utente) {
        this.utente = utente;
    }

    public void setRisultatiBean(List<LezioneBean> risultatiBean) {
        this.risultatiBean = risultatiBean;

        // POPOLA SUBITO LA TABELLA, se è già inizializzata
        if (tabellaLezioni != null) {
            ObservableList<LezioneBean> dati = FXCollections.observableArrayList(risultatiBean);
            tabellaLezioni.setItems(dati);
        }
        aggiornaTabella();
    }
    private void aggiornaTabella() {
        if (tabellaLezioni != null && risultatiBean != null) {
            tabellaLezioni.setItems(FXCollections.observableArrayList(risultatiBean));
        }
    }

    @FXML
    private TableView<LezioneBean> tabellaLezioni;

    @FXML
    private TableColumn<LezioneBean, String> colTipo;

    @FXML
    private TableColumn<LezioneBean, String> colLivello;

    @FXML
    private TableColumn<LezioneBean, String> colGiorno;

    @FXML
    private TableColumn<LezioneBean, String> colFascia;

    @FXML
    private TableColumn<LezioneBean, String> colNote;
    @FXML
    private TableColumn<LezioneBean, Float> colPrezzo;

    @FXML
    private Button btnIndietro;


    @FXML
    private Button btnPrenota;


    @FXML
    public void initialize() {
        // Associa colonne ai campi del bean
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoLezione"));
        colLivello.setCellValueFactory(new PropertyValueFactory<>("livello"));
        colGiorno.setCellValueFactory(new PropertyValueFactory<>("giorni"));
        colFascia.setCellValueFactory(new PropertyValueFactory<>("fasciaOraria"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("noteAggiuntive"));
        colPrezzo.setCellValueFactory(new PropertyValueFactory<>("tariffa"));
        // Popola tabella
        if (risultatiBean != null) {
            ObservableList<LezioneBean> dati = FXCollections.observableArrayList(risultatiBean);
            tabellaLezioni.setItems(dati);
        }
        aggiornaTabella();
    }

    private String convertiGiorni(List<Boolean> giorni) {
        String[] nomiGiorni = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < giorni.size(); i++) {
            if (giorni.get(i)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(nomiGiorni[i]);
            }
        }
        return sb.toString();
    }


    @FXML
    private void prenotaLezione() {
        LezioneBean selezionata = tabellaLezioni.getSelectionModel().getSelectedItem();

        if (selezionata == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nessuna selezione");
            alert.setContentText("Seleziona una lezione dalla tabella.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/confermaprenotazione.fxml"));

            // Carica la view
            Parent root = loader.load();

            // Recupera controller della nuova schermata
            ConfermaPrenotazioneGui controller = loader.getController();
            controller.setUtente(utente);
            controller.setLezione(selezionata);

            // Mostra la nuova finestra
            Stage stage = (Stage) btnPrenota.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tornaIndietro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/cercalezionereal.fxml"));
            loader.setControllerFactory(c -> new CercaLezioneGui(utente));
            Parent root = loader.load();
            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


