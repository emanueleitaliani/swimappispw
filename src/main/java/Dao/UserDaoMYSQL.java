package Dao;

import Exceptions.CredenzialisbagliateException;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;
import Other.Connect;
import Other.Stampa;
import Query.QueryLogin;

import java.util.logging.Logger;
import java.sql.*;

public class UserDaoMYSQL implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoMYSQL.class.getName());
    private static final String ERR_DAO_MSG = "Errore in userDAO: ";
    @Override
    public UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel) throws UtentenonpresenteException, CredenzialisbagliateException {
        UtenteloggatoModel utenteloggatoModel = new UtenteloggatoModel();
        Statement stmt;

        try {
            // Ottieni la connessione al database
            Connection connection = Connect.getInstance().getDBConnection();

            stmt = connection.createStatement();

            String email = credenzialiModel.getEmail();
            String password = credenzialiModel.getPassword();

            // Verifica che l'email esista nel database
            QueryLogin.checkEmail(stmt, email);

            try (ResultSet rs = QueryLogin.loginUser(stmt, email, password)) {
                if (!rs.next()) {
                    throw new CredenzialisbagliateException();
                } else {
                    // Inizializza le credenziali se necessario
                    if (utenteloggatoModel.getCredenziali() == null) {
                        utenteloggatoModel.setCredenziali(new CredenzialiModel());
                    }

                    // Imposta i dati dell'utente
                    utenteloggatoModel.setNome(rs.getString("Nome"));
                    utenteloggatoModel.setCognome(rs.getString("Cognome"));

                    // Imposta l'email e la password dell'utente
                    utenteloggatoModel.getCredenziali().setEmail(rs.getString("Email"));
                    utenteloggatoModel.getCredenziali().setPassword(password); // Imposta la password recuperata

                    // Imposta il ruolo dell'utente
                    utenteloggatoModel.setIstructor(rs.getBoolean("IsIstructor"));
                }
            }
        } catch (SQLException e) {
            logger.severe( ERR_DAO_MSG+ e.getMessage());
        } catch (UtentenonpresenteException e) {
            throw new UtentenonpresenteException();
        }

        return utenteloggatoModel;
    }

    @Override
    public void registrazioneMethod(UtenteloggatoModel registrazioneModel) {
        try (Connection connection = Connect.getInstance().getDBConnection();
             Statement stmt = connection.createStatement()) {

            QueryLogin.registerUser(stmt, registrazioneModel);

        } catch (SQLException e) {
            logger.severe( ERR_DAO_MSG+ e.getMessage());
        }
    }

    public void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException {
        Statement stmt = null;

        try (Connection connection = Connect.getInstance().getDBConnection()) {
            stmt = connection.createStatement();
            String email = registrazioneModel.getCredenziali().getEmail();

            boolean emailInUse = QueryLogin.emailReg(stmt, email);
            if (emailInUse) {
                throw new EmailgiainusoException();
            }

        } catch (SQLException e) {
            logger.severe(ERR_DAO_MSG + e.getMessage());
        } finally {
            closeResources(stmt, null);
        }
    }

    public void registraIstruttoreMethod(String email, String nome, String cognome) {
        Statement stmt = null;

        try (Connection connection = Connect.getInstance().getDBConnection()) {
            stmt = connection.createStatement();
            QueryLogin.registraIstruttore(stmt, email, nome, cognome);

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            closeResources(stmt, null);
        }
    }

    private void handleDAOException(Exception e) {
        Stampa.errorPrint(String.format("UserDAOMySQL: %s", e.getMessage()));
    }

    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            handleDAOException(e);
        }
    }
}
