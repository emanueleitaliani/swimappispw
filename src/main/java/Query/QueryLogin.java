package Query;

import Model.UtenteloggatoModel;

import Other.Stampa;
import Exceptions.CredenzialisbagliateException;
import Exceptions.UtentenonpresenteException;
import Exceptions.EmailgiainusoException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryLogin {

    private QueryLogin(){

    }
    public static void checkEmail(Statement smt, String email) throws UtentenonpresenteException{
        try{
            String sql = String.format(Query.RICERCA_EMAIL, email);
            ResultSet rs = smt.executeQuery(sql);

            if (!rs.next()){
                throw new UtentenonpresenteException();
            }

        } catch (SQLException e){
            throw new UtentenonpresenteException();
        }


    }
    // controlla se la mail è stata registrata
    public static boolean emailReg(Statement smt,String email)throws EmailgiainusoException{
        try{
            String sql = String.format(Query.RICERCA_EMAIL,email);
            ResultSet rs = smt.executeQuery(sql);

            if (rs.next()){
                throw new EmailgiainusoException();
            }
            return false;
        } catch (SQLException e){
            throw new EmailgiainusoException();

        }
    }
    public static ResultSet loginUser(Statement stmt, String email, String password) throws CredenzialisbagliateException{
        try{
            String sql = String.format(Query.VERIFICA_USER, email, password);
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new CredenzialisbagliateException();
        }
    }

    public static void registerUser(Statement stmt, UtenteloggatoModel registrazioneModel){
        try{

            String email = registrazioneModel.getCredenziali().getEmail();
            String nome = registrazioneModel.getNome();
            String cognome = registrazioneModel.getCognome();
            String password = registrazioneModel.getCredenziali().getPassword();
            Boolean IsIstructor = registrazioneModel.isIstructor();



            // Esegui prima l'inserimento nella tabella 'utente'
            String sql = String.format(Query.REGISTRAZIONE, nome, cognome,email,IsIstructor,password);
            stmt.executeUpdate(sql);

        }catch (SQLException e){
            Stampa.errorPrint(String.format("QueryLogin: %s", e.getMessage()));

        }

    }
    public static void registraIstruttore(Statement stmt, String email, String nome, String cognome) {
        String query = String.format(
                "INSERT INTO istruttore (email, nome, cognome) VALUES ('%s', '%s', '%s')",
                email, nome, cognome
        );

        try {
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
                Stampa.println("Istruttore registrato con successo.");
            } else {
                Stampa.println("Nessun istruttore inserito.");
            }
        } catch (SQLException e) {
            Stampa.println("Errore durante la registrazione del tutor: " + e.getMessage());
        }
    }

}



