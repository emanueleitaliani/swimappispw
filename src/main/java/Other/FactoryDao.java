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
        return null;
    }

    public static PrenotazioneDao getPrenotazioneDao() {
        String daoType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoMYSQL();
        } else if (MEMORY.equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoInMemory();
        }
        return null;
    }
}