package Gui;

import Bean.LezioneBean;
import Bean.Utenteloggatobean;
import Controller.Prenotazionecontroller;
import Exceptions.LezioniNonTrovateException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CercaLezioneGui extends HomeUtenteGui {

    private static final String SELEZIONE_MANCANTE = "Selezione mancante";
    // Tipo lezione
    @FXML
    private CheckBox lezionePrivata;
    @FXML
    private CheckBox lezioneGruppo;

    // Livello lezione
    @FXML
    private CheckBox livelloPrincipiante;
    @FXML
    private CheckBox livelloIntermedio;
    @FXML
    private CheckBox livelloAgonistica;

    // Giorni
    @FXML
    private CheckBox lunedi;
    @FXML
    private CheckBox martedi;
    @FXML
    private CheckBox mercoledi;
    @FXML
    private CheckBox giovedi;
    @FXML
    private CheckBox venerdi;
    @FXML
    private CheckBox sabato;
    @FXML
    private CheckBox domenica;

    // Fascia oraria e note
    @FXML
    private TextField fasciaOraria;
    @FXML
    private TextField noteAggiuntive;

    // Prezzo massimo
    @FXML
    private TextField prezzoMassimo;

    @FXML
    private Button btnIndietro;
    @FXML
    private Button btnConferma;

    private String tipoLezione;
    private String livello;



    List<LezioneBean> risultatiBean = new ArrayList<>();

    public CercaLezioneGui(Utenteloggatobean utente) {
        this.utente = utente;
    }

    @FXML
    private void cercaLezione() {

        LezioneBean lezioneBean = new LezioneBean();

        // CONTROLLO TIPO LEZIONE
        if (!lezionePrivata.isSelected() && !lezioneGruppo.isSelected()) {
            showAlert(Alert.AlertType.WARNING, SELEZIONE_MANCANTE, "Seleziona almeno un tipo di lezione.");
            return;
        }

        // CONTROLLO LIVELLO
        if (!livelloPrincipiante.isSelected() && !livelloIntermedio.isSelected() && !livelloAgonistica.isSelected()) {
            showAlert(Alert.AlertType.WARNING, SELEZIONE_MANCANTE, "Seleziona almeno un livello.");
            return;
        }

        // CONTROLLO GIORNI
        if (!lunedi.isSelected() && !martedi.isSelected() && !mercoledi.isSelected() &&
                !giovedi.isSelected() && !venerdi.isSelected() &&
                !sabato.isSelected() && !domenica.isSelected()) {
            showAlert(Alert.AlertType.WARNING, SELEZIONE_MANCANTE, "Seleziona almeno un giorno.");
            return;
        }

        // Impostazione tipo lezione
        if (lezionePrivata.isSelected()) {
            tipoLezione = "Privata";
        } else if (lezioneGruppo.isSelected()) {
            tipoLezione = "In Gruppo";
        }

        lezioneBean.setTipoLezione(tipoLezione);

        // Impostazione livello
        if (livelloPrincipiante.isSelected()) {
            livello = "Principiante";
        } else if (livelloIntermedio.isSelected()) {
            livello = "Intermedio";
        } else if (livelloAgonistica.isSelected()) {
            livello = "Agonista";
        }

        lezioneBean.setLivello(livello);

        // Impostazione giorni
        List<Boolean> giorni = List.of(
                lunedi.isSelected(),
                martedi.isSelected(),
                mercoledi.isSelected(),
                giovedi.isSelected(),
                venerdi.isSelected(),
                sabato.isSelected(),
                domenica.isSelected()
        );

        lezioneBean.setGiorni(giorni);

        // Impostazione altri campi
        lezioneBean.setFasciaOraria(fasciaOraria.getText().trim());
        lezioneBean.setNoteAggiuntive(noteAggiuntive.getText().trim());

        // Controllo prezzo
        String prezzoStr = prezzoMassimo.getText().trim();

        if (!prezzoStr.isEmpty()) {
            try {
                float prezzo = Float.parseFloat(prezzoStr);
                lezioneBean.setTariffa(prezzo);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Errore formato prezzo", "Inserisci un prezzo valido (es: 25.5)");
                return;
            }
        }

        // Ricerca dal controller


        // Cambio schermata
        try {
            Prenotazionecontroller prenotazione = new Prenotazionecontroller();
            risultatiBean = prenotazione.ricercaLezione(lezioneBean);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/prenotalezione.fxml"));
            Parent root = loader.load();

            PrenotaLezioneGui controller = loader.getController();
            controller.setUtente(utente);
            controller.setRisultatiBean(risultatiBean);

            Stage stage = (Stage) btnConferma.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }catch (LezioniNonTrovateException ex) {
            // Gestione specifica: l'utente vede l'alert informativo
            showAlert(Alert.AlertType.INFORMATION, "Nessun Risultato", ex.getMessage());

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel caricamento della schermata.");

        }
    }

    @FXML
    private void tornaIndietro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/homeutente.fxml"));
            loader.setControllerFactory(c -> new HomeUtenteGui(utente));
            Parent root = loader.load();

            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel ritorno alla Home.");

        }
    }

    // ---------- METODO PER ALERT ----------

    private void showAlert(Alert.AlertType type, String titolo, String messaggio) {
        Alert alert = new Alert(type);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}