package Model;


public class UtenteloggatoModel {
    private CredenzialiModel credenziali;
    private String Nome;
    private String Cognome;
    protected boolean isIstructor;

    public UtenteloggatoModel() {}
    public UtenteloggatoModel(CredenzialiModel credenziali, String Nome, String Cognome, boolean isIstructor){
        this.credenziali = credenziali;
        this.Nome = Nome;
        this.Cognome = Cognome;
        this.isIstructor = isIstructor;
    }
    public CredenzialiModel getCredenziali(){
        return credenziali;
    }
    public void setCredenziali(CredenzialiModel credenziali) {
        this.credenziali = credenziali;
    }
    public String getNome(){
        return Nome;
    }
    public void setNome(String Nome) {
        this.Nome = Nome;
    }
    public String getCognome(){
        return Cognome;
    }
    public void setCognome(String Cognome) {
        this.Cognome = Cognome;
    }
    public boolean isIstructor(){
        return isIstructor;
    }
    public void setIstructor(boolean isIstructor) {
        this.isIstructor = isIstructor;
    }
}
