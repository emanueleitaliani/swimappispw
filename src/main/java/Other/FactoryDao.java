package Other;

import Dao.*;
import InMemory.*;

public class FactoryDao {


    private static final String MYSQL = "mysql";
    private static final String MEMORY = "memory";
    private static final String JSON = "json";

    private FactoryDao() {}

    public static UserDao getUserDAO() {
        String daoType = Config.getPersistenceType();
        // 2. Usa le costanti nei confronti
        if (MYSQL.equalsIgnoreCase(daoType)) {
            return new UserDaoMYSQL();
        } else if (JSON.equalsIgnoreCase(daoType)) {
            return new UserDAOJSON();
        } else if (MEMORY.equalsIgnoreCase(daoType)) {
            return new UserDaoInMemory();
        }
        return null;
    }

    public static LezioneDao getLezioneDao() {
        String daoType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daoType)) {
            return new LezioneDaoMYSQL();
        } else if (MEMORY.equalsIgnoreCase(daoType)) {
            return new LezioneDaoInMemory();
        }
        else if (JSON.equalsIgnoreCase(daoType)) {
        throw new UnsupportedOperationException("La persistenza JSON non supporta la gestione delle lezioni.");
        }
        return null;

        }

    public static PrenotazioneDao getPrenotazioneDao() {
        String daoType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoMYSQL();
        } else if (MEMORY.equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoInMemory();
        } else if (JSON.equalsIgnoreCase(daoType)) {
        // GESTIONE CASO MANCANTE: Blocca esplicitamente se si tenta di usare le prenotazioni in JSON
        throw new UnsupportedOperationException("La persistenza JSON non supporta la gestione delle prenotazioni.");
    }
        return null;
    }
}