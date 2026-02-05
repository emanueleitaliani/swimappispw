package Bean;

public class Utenteloggatobean{
    private final CredenzialiBean credenzialiBean;
    private String nome;
    private String cognome;
    protected boolean isIstructor;

    public Utenteloggatobean(CredenzialiBean credenzialiBean, String nome, String cognome, boolean ruolo){
        this.credenzialiBean=credenzialiBean;
        this.nome = nome;
        this.cognome = cognome;
        this.isIstructor = ruolo;
    }

    public Utenteloggatobean(CredenzialiBean credenzialiBean, String nome, String cognome){
        this.credenzialiBean=credenzialiBean;
        this.nome = nome;
        this.cognome = cognome;
    }
    public CredenzialiBean getCredenziali(){
        return credenzialiBean;
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


    public void setRuolo(boolean role) {
        this.isIstructor = role;
    }
    public boolean getRuolo() {
        return this.isIstructor;
    }

    public boolean isIstructor() {
        return this.isIstructor;
    }


}
