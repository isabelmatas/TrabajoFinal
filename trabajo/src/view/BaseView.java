package view;
import controller.Controller;

public abstract class BaseView
{
    public Controller controller; 

    public BaseView(Controller controller)
    {
        this.controller = controller;
    }

    public abstract void init();
    public abstract void showMessage(String message);
    public abstract void showErrorMessage(String error);
    public abstract void end(); 
}