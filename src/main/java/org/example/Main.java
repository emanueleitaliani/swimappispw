package org.example;
import Other.Config;
import Other.Connect;
import Other.Stampa;
import Pattern.StateMachineImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import static javafx.application.Application.launch;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    private static String persistenceType; // "mysql", "json" o "memory"

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException{
        // Inizializza i mapper
        Config.loadFromFile();

        // Scanner per input utente
        Scanner scanner = new Scanner(System.in);

        // 1️⃣ Prima scelta: versione DB o Memory
        boolean validChoice = false;
        while (!validChoice) {
            Stampa.println("Scegli la versione dell'applicazione:");
            Stampa.println("1. MySQL (DAO reale)");
            Stampa.println("2. In Memory (tutti i DAO in memoria)");
            Stampa.println("3. JSON(LOGIN DAO)");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consuma newline
                if (choice == 1) {
                    Config.setPersistenceType("mysql");
                    validChoice = true;
                } else if (choice == 2) {
                    Config.setPersistenceType("memory");
                    validChoice = true;
                } else if (choice == 3) {
                    Config.setPersistenceType("json");
                    validChoice = true;
                } else {
                    Stampa.println("Scelta non valida, riprova.");
                }
            } catch (Exception e) {
                Stampa.println("Errore input: " + e.getMessage());
                scanner.nextLine(); // pulisce input
            }
        }



        if ("mysql".equalsIgnoreCase(Config.getPersistenceType())) {
            testDatabaseConnection();
        }


        boolean validInput = false;
        while (!validInput) {
            try {
                mostraMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consuma newline
                switch (choice) {
                    case 1:
                        Stampa.println("ciao");
                        interfacciaGrafica(stage);
                        validInput = true;
                        break;
                    case 2:
                        interfaceCLI();
                        validInput = true;
                        break;
                    default:
                        Stampa.println("Scelta non valida");
                }
            } catch (Exception e) {
                Stampa.errorPrint("Errore: " + e.getMessage());
                scanner.nextLine();  // Pulisce input in caso di errore
            }
        }
    }
    public void interfacciaGrafica(Stage stage) throws IOException{
        URL fxmlUrl = Main.class.getResource("/Fxml/login.fxml");
        if (fxmlUrl == null) {
            throw new IOException("❌ File FXML non trovato: /gui/login.fxml");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Swimapp");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Stage secondoStage = new Stage();
        FXMLLoader secondofxmlLoader = new FXMLLoader(Main.class.getResource("/Fxml/login.fxml"));
        Scene secondaScene = new Scene(secondofxmlLoader.load());
        secondoStage.setTitle("Swimapp");
        secondoStage.setResizable(false);
        secondoStage.setScene(secondaScene);
        secondoStage.show();
    }

    public  void interfaceCLI(){
        StateMachineImpl context= new StateMachineImpl();
        while(context.getState()!=null) {
            context.goNext();

        }
        Stampa.println("Arrivederci");
    }
    public void mostraMenu() {
        Stampa.println(" ");
        Stampa.println("-------------- Swimapp --------------");
        Stampa.println("Scegli l'interfaccia da utilizzare:");
        Stampa.println("1. Interfaccia grafica");
        Stampa.println("2. Interfaccia a riga di comando");

    }
    private void testDatabaseConnection() {
        try {
            Connection conn = Connect.getInstance().getDBConnection();
            if (conn != null && !conn.isClosed()) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT 1")) {
                    if (rs.next()) {
                        Stampa.println("✅ Connessione al database verificata con successo.");
                    }
                }
            } else {
                Stampa.errorPrint("❌ Connessione al database non disponibile.");
            }
        } catch (Exception e) {
            Stampa.errorPrint("❌ Errore durante il test di connessione: " + e.getMessage());
        }
    }


}
