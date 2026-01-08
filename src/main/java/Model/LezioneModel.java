package Model;


public class LezioneModel {
    private String giorniDisponibili;
    private Float tariffa;
    private String emailIstruttore;
    private String nomeIstruttore;
    private String cognomeIstruttore;
    private String tipoLezione;
    private String livello;
    private String fasciaOraria;
    private String note;

    public LezioneModel() {}

    public LezioneModel(String tipoLezione,
                        String giorniDisponibili,
                        Float tariffa,
                        UtenteloggatoModel istruttore,
                        String livello,
                        String fasciaOraria,
                        String note) {
        this.tipoLezione = tipoLezione;
        this.giorniDisponibili = giorniDisponibili;
        this.tariffa = tariffa;
        this.nomeIstruttore = istruttore.getNome();
        this.cognomeIstruttore = istruttore.getCognome();
        this.emailIstruttore = istruttore.getCredenziali().getEmail();
        this.livello = livello;
        this.fasciaOraria = fasciaOraria;
        this.note = note;
    }

    // Getters e Setters

    public String getGiorniDisponibili() {
        return giorniDisponibili;
    }

    public void setGiorniDisponibili(String giorniDisponibili) {
        this.giorniDisponibili = giorniDisponibili;
    }

    public Float getTariffa() {
        return tariffa;
    }

    public void setTariffa(Float tariffa) {
        this.tariffa = tariffa;
    }

    public String getEmailIstruttore() {
        return emailIstruttore;
    }
    public void setEmailIstruttore(String emailIstruttore) {
        this.emailIstruttore = emailIstruttore;

    }
    public String getNomeIstruttore() {
        return nomeIstruttore;

    }
    public void setNomeIstruttore(String nomeIstruttore) {
        this.nomeIstruttore=nomeIstruttore;

    }

    public String getCognomeIstruttore() {
        return cognomeIstruttore;
    }
    public void setCognomeIstruttore(String cognomeIstruttore) {
        this.cognomeIstruttore = cognomeIstruttore;
    }
    public String getTipoLezione() {
        return tipoLezione;
    }

    public void setTipoLezione(String tipoLezione) {
        this.tipoLezione = tipoLezione;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public String getFasciaOraria() {
        return fasciaOraria;
    }

    public void setFasciaOraria(String fasciaOraria) {
        this.fasciaOraria = fasciaOraria;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
