package Bean;

import java.util.List;

public class LezioneBean {
    private String giorni;
    private Float tariffa;
    private String emailIstruttore;
    private String nomeIstruttore;
    private String cognomeIstruttore;
    private String tipoLezione; // ad esempio: "individuale", "gruppo", "correttiva", ecc.
    private String livello;     // ad esempio: "principiante", "intermedio", "agonistico"
    private String fasciaOraria; // es: "10:00-11:00"
    private String noteAggiuntive;

    public LezioneBean() {}

    public LezioneBean(String tipoLezione,
                       String giorni, Float tariffa,Utenteloggatobean istruttore, String livello, String fasciaOraria, String noteAggiuntive) {
        this.tipoLezione = tipoLezione;
        this.giorni = giorni;
        this.tariffa = tariffa;
        this.emailIstruttore = istruttore.getCredenziali().getEmail();
        this.nomeIstruttore = istruttore.getNome();
        this.cognomeIstruttore = istruttore.getCognome();
        this.livello = livello;
        this.fasciaOraria = fasciaOraria;
        this.noteAggiuntive = noteAggiuntive;
    }

    // Conversione lista giorni booleani → stringa leggibile
    public String convertiGiorni(List<Boolean> giorniBool) {
        String[] giorniSettimana = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < giorniBool.size(); i++) {
            if (Boolean.TRUE.equals(giorniBool.get(i))) {
                if (!result.isEmpty()) result.append(", ");
                result.append(giorniSettimana[i]);
            }
        }
        return result.toString();
    }






    public void setGiorni(List<Boolean> giorniBool) {
        this.giorni = convertiGiorni(giorniBool);
    }
    public String getGiorni() { return giorni; }


    public Float getTariffa() { return tariffa; }
    public void setTariffa(Float tariffa) { this.tariffa = tariffa; }

    public String getEmailIstruttore() { return emailIstruttore; }


    public String getNomeIstruttore() { return nomeIstruttore; }


    public String getCognomeIstruttore() { return cognomeIstruttore; }


    public String getTipoLezione() { return tipoLezione; }
    public void setTipoLezione(String tipoLezione) { this.tipoLezione = tipoLezione; }

    public String getLivello() { return livello; }
    public void setLivello(String livello) { this.livello = livello; }

    public String getFasciaOraria() { return fasciaOraria; }
    public void setFasciaOraria(String fasciaOraria) { this.fasciaOraria = fasciaOraria; }

    public String getNoteAggiuntive() { return noteAggiuntive; }
    public void setNoteAggiuntive(String noteAggiuntive) { this.noteAggiuntive = noteAggiuntive; }

}

