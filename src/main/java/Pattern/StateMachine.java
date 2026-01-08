package Pattern;

public interface StateMachine {


    /** Esegue l'azione dello stato corrente */
    public void goNext();

    /** Torna allo stato precedente */
    public void goBack();

    /** Effettua la transizione verso un nuovo stato */
    public void transition(AbstractState nextState);
    /** Ottiene lo stato corrente */
    public void setState();

    AbstractState getState();
    void start();
}
