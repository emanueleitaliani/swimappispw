package Other;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "src/main/resources/resources/config.properties";
    private static final Properties properties = new Properties();

    private static String persistenceType;// default

    public static void loadFromFile() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) { // 2. Usa la costante qui!
            properties.load(fis);
            persistenceType = properties.getProperty("persistence.type", "mysql");
        } catch (IOException e) {

            Stampa.println("Errore lettura " + CONFIG_FILE + ", uso default mysql");
            persistenceType = "mysql";
        }
    }

    public static String getPersistenceType() {
        return persistenceType;
    }

    public static void setPersistenceType(String type) {
        persistenceType = type;
    }
}

