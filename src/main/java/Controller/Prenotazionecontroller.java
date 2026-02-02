package Controller;
import Bean.*;
import Dao.LezioneDao;

import Dao.PrenotazioneDao;

import Exceptions.EmailnonvalidaException;
import Exceptions.LezioneGiaPrenotataException;
import Exceptions.LezioniNonTrovateException;
import Exceptions.UtentenonpresenteException;
import Model.LezioneModel;
import Model.PrenotazioneModel;
import Other.FactoryDao;
import Other.StatoPrenotazione;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Prenotazionecontroller {


    private final List<LezioneBean> risultatiRicercaBean = new ArrayList<>();
    private final PrenotazioneDao prenotazioneDao;
    private final LezioneDao lezioneDao;
    // ✅ Controlla che l'email esista nel database


    public Prenotazionecontroller() {
        this.prenotazioneDao = FactoryDao.getPrenotazioneDao();
        this.lezioneDao = FactoryDao.getLezioneDao();
    }


    public void controllaEmail(String nome, String cognome, String email) throws UtentenonpresenteException {
        boolean esiste = lezioneDao.controllaEmail(nome, cognome, email);
        if (!esiste) {
            throw new UtentenonpresenteException();
        }
    }

    public List<LezioneBean> ricercaLezione(LezioneBean filtriBean) throws LezioniNonTrovateException{

        LezioneModel lezioneinfo = new LezioneModel();

        lezioneinfo.setFasciaOraria(filtriBean.getFasciaOraria());
        lezioneinfo.setLivello(filtriBean.getLivello());
        lezioneinfo.setTariffa(filtriBean.getTariffa());
        lezioneinfo.setTipoLezione(filtriBean.getTipoLezione());
        lezioneinfo.setNote(filtriBean.getNoteAggiuntive());
        lezioneinfo.setGiorniDisponibili(filtriBean.getGiorni());

        // 2. Chiedi alla DAO le lezioni che corrispondono ai filtri
        List<LezioneModel> risultatiModel = lezioneDao.cercaLezione(lezioneinfo);

        if (risultatiModel.isEmpty()) {
            throw new LezioniNonTrovateException("Nessuna lezione trovata con i filtri inseriti.");
        }

        for (LezioneModel risultato : risultatiModel) {
            CredenzialiBean credenzialiBean = new CredenzialiBean(risultato.getEmailIstruttore(), null);
            Utenteloggatobean utente = new Utenteloggatobean(credenzialiBean, risultato.getNomeIstruttore(), risultato.getCognomeIstruttore());
            LezioneBean risultatoBean = new LezioneBean(risultato.getTipoLezione(), risultato.getGiorniDisponibili(), risultato.getTariffa(), utente, risultato.getLivello(), risultato.getFasciaOraria(), risultato.getNote());
            risultatiRicercaBean.add(risultatoBean);
        }

        return risultatiRicercaBean;
    }

    // ✅ Inserisce la prenotazione nel DB
    public void richiediprenotazione(Prenotazionebean prenotazione) throws SQLException,LezioneGiaPrenotataException {



        PrenotazioneModel prenotazioneModel = new PrenotazioneModel();
        prenotazioneModel.setIdPrenotazione(prenotazione.getIdPrenotazione());
        prenotazioneModel.setNome(prenotazione.getNome());
        prenotazioneModel.setCognome(prenotazione.getCognome());
        prenotazioneModel.setEmailIstruttore(prenotazione.getEmailIstruttore());
        prenotazioneModel.setEmailUtente(prenotazione.getEmailUser());
        prenotazioneModel.setPrezzo(prenotazione.getPrezzo());
        prenotazioneModel.setGiorno(prenotazione.getGiorno());
        prenotazioneModel.setInfo(prenotazione.getInfo());
        prenotazioneModel.setOra(prenotazione.getHour());
        boolean esistente = prenotazioneDao.isGiaPrenotata(
                prenotazione.getEmailUser(),
                prenotazione.getEmailIstruttore(),
                prenotazione.getGiorno(),
                prenotazione.getHour()
        );

        if (esistente) {
            throw new LezioneGiaPrenotataException("Hai già prenotato questa lezione per il giorno " +
                    prenotazione.getGiorno() + " alle ore " + prenotazione.getHour());
        }

        prenotazioneDao.prenota(prenotazioneModel);
    }

    // ritorna le prenotazioni in base alle mail
    public List<Prenotazionebean> getPrenotazioniByEmail(String emailUtente) throws UtentenonpresenteException,SQLException {
        // 1. Chiedi i dati al DAO (ricevi Model)
        List<PrenotazioneModel> models = prenotazioneDao.getPrenotazioniByEmail(emailUtente);
        List<Prenotazionebean> beans = new ArrayList<>();

        // 2. Trasforma ogni Model in Bean
        for (PrenotazioneModel m : models) {
            Prenotazionebean bean = new Prenotazionebean();

            bean.setIdPrenotazione(m.getIdPrenotazione());
            bean.setNome(m.getNome());
            bean.setCognome(m.getCognome());
            bean.setEmailIstruttore(m.getEmailIstruttore());
            bean.setEmailUser(m.getEmailUtente()); // Nota: emailUtente nel Model diventa emailUser nel Bean
            bean.setPrezzo(m.getPrezzo());
            bean.setGiorno(m.getGiorno());
            bean.setInfo(m.getInfo());
            bean.setHour(m.getOra());
            bean.setStatus(m.getStatus());

            beans.add(bean);
        }

        return beans;
    }

    public boolean cancellaPrenotazioneById(int IdPrenotazione, String Mailutente) throws SQLException, UtentenonpresenteException {
        // Conversione da Bean a Model

        return prenotazioneDao.deletePrenotazioneById(IdPrenotazione, Mailutente);
    }

    public List<Prenotazionebean> getPrenotazioniIstruttore(String emailIstruttore) {
        List<Prenotazionebean> listaBean = new ArrayList<>();
        try {
            List<PrenotazioneModel> modelli = prenotazioneDao.getPrenotazioniPerIstruttore(emailIstruttore);
            for (PrenotazioneModel m : modelli) {
                Prenotazionebean b = new Prenotazionebean();
                b.setIdPrenotazione(m.getIdPrenotazione());
                b.setEmailUser(m.getEmailUtente());
                b.setNome(m.getNome()); // Nome Studente se necessario
                b.setCognome(m.getCognome());
                b.setGiorno(m.getGiorno());
                b.setHour(m.getOra());
                b.setInfo(m.getInfo());
                b.setPrezzo(m.getPrezzo());
                b.setStatus(m.getStatus()); // Mapping Enum -> Enum (perfetto!)
                listaBean.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaBean;
    }

    public void aggiornaStatoPrenotazione(int idPrenotazione, StatoPrenotazione nuovoStato) {
        try {
            prenotazioneDao.updateStato(idPrenotazione, nuovoStato);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}