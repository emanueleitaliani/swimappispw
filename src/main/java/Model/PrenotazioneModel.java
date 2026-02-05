package Model;


import Other.StatoPrenotazione;

public class PrenotazioneModel {
    private int idPrenotazione;
    private String nome;
    private String cognome;
    private String emailIstruttore;
    private String emailUtente;
    private float prezzo;
    private String giorno;
    private String info;
    private float ora;
    private StatoPrenotazione status;

    public PrenotazioneModel() {
        //costruttore vuoto
    };

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
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
    public String getEmailUtente() {
        return emailUtente;
    }
    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
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
    public float getOra(){
        return ora;

    }
    public void setOra(float ora){
        this.ora=ora;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public StatoPrenotazione getStatus() { return status; }
    public void setStatus(StatoPrenotazione status) { this.status = status; }

}
