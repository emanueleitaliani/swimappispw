package Pattern;

public abstract class AbstractState {

    protected AbstractState(){

    }

    public void entry(StateMachineImpl context) {}

    public void exit(StateMachineImpl context) {}

    protected void goBack(StateMachineImpl context) {
        context.goBack();
    }


    // Cambio stato
    protected void goNext(StateMachineImpl context, AbstractState nextState) {
        context.transition(nextState);

    }

    public abstract void action(StateMachineImpl context);

    public void mostraSchermata() {}

    // Metodo per stampare il messaggio di benvenuto
    public void stampaBenvenuto() {}


}
