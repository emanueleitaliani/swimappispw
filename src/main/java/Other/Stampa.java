package Other;
public class Stampa{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET1 = "\u001B[0m";
    private Stampa(){}

    public static void print(String message){

        System.out.print(message);
    }
    public static void println(String message){

        System.out.println(message);
    }
    public static void printlnBlu(String message) {

        System.out.println(ANSI_BLUE + message + ANSI_RESET1);

    }
    public static void errorPrint(String message) {

        System.out.println(ANSI_RED + message + ANSI_RESET);

    }

}