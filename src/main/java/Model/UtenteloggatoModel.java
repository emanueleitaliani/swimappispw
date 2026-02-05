package Model;


public class UtenteloggatoModel {
    private CredenzialiModel credenziali;
    private String nome;
    private String cognome;
    protected boolean isIstructor;

    public UtenteloggatoModel() {}
    public UtenteloggatoModel(CredenzialiModel credenziali, String nome, String cognome, boolean isIstructor){
        this.credenziali = credenziali;
        this.nome = nome;
        this.cognome = cognome;
        this.isIstructor = isIstructor;
    }
    public CredenzialiModel getCredenziali(){
        return credenziali;
    }
    public void setCredenziali(CredenzialiModel credenziali) {
        this.credenziali = credenziali;
    }
    public String getNome(){
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
    public boolean isIstructor(){
        return isIstructor;
    }
    public void setIstructor(boolean isIstructor) {
        this.isIstructor = isIstructor;
    }
}
