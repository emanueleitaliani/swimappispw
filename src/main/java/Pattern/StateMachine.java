package Pattern;

public interface StateMachine {


    /** Esegue l'azione dello stato corrente */
    void goNext();

    /** Torna allo stato precedente */
    void goBack();

    /** Effettua la transizione verso un nuovo stato */
    void transition(AbstractState nextState);
    /** Ottiene lo stato corrente */
    void setState();

    AbstractState getState();
    void start();
}
