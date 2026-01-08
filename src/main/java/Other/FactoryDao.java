package Other;

import Dao.*;
import InMemory.*;

public class FactoryDao {

    private FactoryDao() {}

    public static UserDao getUserDAO() {
        String daoType = Config.getPersistenceType();
        if ("mysql".equalsIgnoreCase(daoType)) {
            return new UserDaoMYSQL();
        } else if ("json".equalsIgnoreCase(daoType)) {
            return new UserDAOJSON();
        } else if ("memory".equalsIgnoreCase(daoType)) {
            return new UserDaoInMemory();
        }
        return null;
    }

    public static LezioneDao getLezioneDao() {
        String daoType = Config.getPersistenceType();
        if ("mysql".equalsIgnoreCase(daoType)) {
            return new LezioneDaoMYSQL();
        } else if ("memory".equalsIgnoreCase(daoType)) {
            return new LezioneDaoInMemory();
        }
        return null;
    }

    public static PrenotazioneDao getPrenotazioneDao() {
        String daoType = Config.getPersistenceType();
        if ("mysql".equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoMYSQL();
        } else if ("memory".equalsIgnoreCase(daoType)) {
            return new PrenotazioneDaoInMemory();
        }
        return null;
    }
}

