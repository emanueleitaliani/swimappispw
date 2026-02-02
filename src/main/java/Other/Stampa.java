package Other;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Stampa {

    // Definiamo il logger per questa classe
    private static final Logger LOGGER = Logger.getLogger(Stampa.class.getName());

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private Stampa() {
        throw new IllegalStateException("Utility class");
    }

    // Stampa generica (livello INFO)
    public static void print(String message) {
        // Il logger di default va sempre a capo, ma possiamo usare log per messaggi informativi
        LOGGER.log(Level.INFO, "{0}", message);
    }

    // Stampa e va a capo
    public static void println(String message) {
        LOGGER.log(Level.INFO, "{0}", message);
    }

    // Stampa la guida per ogni pagina in CLI (Blu)
    public static void printlnBlu(String message) {
        if (LOGGER.isLoggable(Level.INFO)) {
            String formattedMessage = String.format("%s%s%s", ANSI_BLUE, message, ANSI_RESET);
            LOGGER.info(formattedMessage);
        }
    }

    public static void printBlu(String message) {
        if (LOGGER.isLoggable(Level.INFO)) {
            String formattedMessage = String.format("%s%s%s", ANSI_BLUE, message, ANSI_RESET);
            LOGGER.info(formattedMessage);
        }
    }

    // Stampa messaggio di errore (livello SEVERE)
    public static void errorPrint(String message) {
        if (LOGGER.isLoggable(Level.SEVERE)) {
            String formattedMessage = String.format("%s%s%s", ANSI_RED, message, ANSI_RESET);
            LOGGER.severe(formattedMessage);
        }
    }
}
