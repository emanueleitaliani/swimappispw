package Bean;

public class Utenteloggatobean{
    private CredenzialiBean credenzialiBean;
    private String nome;
    private String cognome;
    protected boolean isIstructor;

    public Utenteloggatobean(CredenzialiBean credenzialiBean, String nome, String cognome, boolean ruolo){
        this.credenzialiBean=credenzialiBean;
        this.nome = nome;
        this.cognome = cognome;
        this.isIstructor = ruolo;
    }

    public Utenteloggatobean(CredenzialiBean credenzialiBean, String Nome, String Cognome){
        this.credenzialiBean=credenzialiBean;
        this.nome = nome;
        this.cognome = cognome;
    }
    public CredenzialiBean getCredenziali(){
        return credenzialiBean;
    }
    public void setCredenziali(CredenzialiBean credenzialiBean){
        this.credenzialiBean = credenzialiBean;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String Nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String Cognome) {
        this.cognome = Cognome;
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
