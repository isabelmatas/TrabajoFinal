package view;
import controller.Controller;

public abstract class BaseView
{
    public Controller controlador; 

    public BaseView(Controller controlador)
    {
        this.controlador = controlador;
    }

    public abstract void init();
    public abstract void showMessage(String message);
    public abstract void showErrorMessage(String error);
    public abstract void end(); 
}