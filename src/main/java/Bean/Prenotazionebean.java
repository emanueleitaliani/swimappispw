package Bean;
import Other.StatoPrenotazione;
public class Prenotazionebean {
    private int idPrenotazione;
    private String nome;
    private String cognome;
    private String emailIstruttore;
    private String emailUser;
    private String giorno;
    private String info;
    private float prezzo;
    private float hour ;
    private StatoPrenotazione status;

    public Prenotazionebean() {
        // Costruttore vuoto richiesto per la serializzazione o dai framework
    }



    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome(){
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public String getEmailIstruttore() {
        return emailIstruttore;
    }

    public void setEmailIstruttore(String emailIstruttore) {
        this.emailIstruttore = emailIstruttore;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public StatoPrenotazione getStatus() { return status; }
    public void setStatus(StatoPrenotazione status) { this.status = status; }

}


