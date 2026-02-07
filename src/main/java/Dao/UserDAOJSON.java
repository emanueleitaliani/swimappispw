package Dao;
import Exceptions.EmailgiainusoException;
import Exceptions.UtentenonpresenteException;
import Exceptions.CredenzialisbagliateException;
import Model.CredenzialiModel;
import Model.UtenteloggatoModel;
import Other.Stampa;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class UserDAOJSON implements UserDao {
    private static final String FILE_PATH = System.getProperty("user.home") + "/swimapp_users.txt";

    private final Map<String, UtenteloggatoModel> users = new HashMap<>();

    public UserDAOJSON() {
        loadFromFile();
    }


    public UtenteloggatoModel loginMethod(CredenzialiModel credenzialiModel) throws CredenzialisbagliateException, UtentenonpresenteException {
        UtenteloggatoModel user = users.get(credenzialiModel.getEmail());
        if (user == null) {
            throw new UtentenonpresenteException();
        }
        if (!user.getCredenziali().getPassword().equals(credenzialiModel.getPassword())) {
            throw new CredenzialisbagliateException();
        }
        return user;
    }


    public void registrazioneMethod(UtenteloggatoModel registrazioneModel) {
        users.put(registrazioneModel.getCredenziali().getEmail(), registrazioneModel);
        saveToFile();
    }


    public void controllaEmailMethod(UtenteloggatoModel registrazioneModel) throws EmailgiainusoException {
        if (users.containsKey(registrazioneModel.getCredenziali().getEmail())) {
            throw new EmailgiainusoException();
        }
    }

    //eventualmente modificare fromString
    public void registraIstruttoreMethod(String email,String nome, String cognome){
        UtenteloggatoModel istruttore = users.get(email);
        if (istruttore != null) {
            istruttore.setNome(nome);
            istruttore.setCognome(cognome);
            istruttore.isIstructor();
            saveToFile();
        }
    }
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    String email = parts[0].trim();
                    String password = parts[1].trim();
                    String nome = parts[2].trim();
                    String cognome = parts[3].trim();
                    boolean isIstructor = Boolean.parseBoolean(parts[4].trim());

                    CredenzialiModel credenziali = new CredenzialiModel(email, password);
                    UtenteloggatoModel user = new UtenteloggatoModel(credenziali, nome, cognome, isIstructor);

                    users.put(email, user);
                }
            }
        } catch (IOException e) {
            Stampa.errorPrint("Impossibile caricare gli utenti dal file utenti.");
        }
    }
    private void saveToFile() {
        // 1. Verifica che la mappa non sia vuota prima di scrivere (opzionale, per debug)
        if (users.isEmpty()) {
            Stampa.errorPrint("Attenzione: la mappa utenti è vuota. Il file verrà svuotato.");
        }

        // 2. Il Try-with-resources chiude automaticamente il writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (UtenteloggatoModel user : users.values()) {

                // 3. Controllo di sicurezza: se l'utente o le credenziali sono null, salta la riga
                if (user == null || user.getCredenziali() == null) continue;

                CredenzialiModel c = user.getCredenziali();

                // 4. Costruiamo la stringa in modo pulito
                String line = String.format("%s,%s,%s,%s,%b",
                        c.getEmail().trim(),
                        c.getPassword().trim(),
                        user.getNome().trim(),
                        user.getCognome().trim(),
                        user.isIstructor() // Il %b trasforma il boolean in "true" o "false"
                );

                writer.write(line);
                writer.newLine();
            }

            // 5. IMPORTANTISSIMO: Forza la scrittura dei dati rimasti nel buffer
            writer.flush();

            Stampa.println("Dati salvati correttamente in: " + FILE_PATH);

        } catch (IOException e) {
            // 6. Stampa l'errore REALE in console per poter fare debug
            e.printStackTrace();
            Stampa.errorPrint("Errore critico nel salvataggio: " + e.getMessage());
        }
    }



}
