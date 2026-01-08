package Exceptions;

public class DaoException extends Exception{
    public DaoException(){
        super("Dao non risponde");
    }
}
