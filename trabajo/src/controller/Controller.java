package controller;
import view.BaseView;
import java.util.ArrayList;

import model.IRepository;
import model.RepositoryException;
import model.Task;

public class Controller
{
    private IRepository repository;
    private BaseView view;
    
    public Controller(IRepository repository, BaseView view)
    {
        this.repository = repository;
        this.view = view;
    }

    public void iniciarApliacion()
    {
        try
        {
            repository.getAllTask();
            view.showMessage("Se han cargado correctamente los datos");
        }
        catch(RepositoryException e)
        {
            view.showErrorMessage("Error al cargar los datos");
        }
        view.init();
    }


    public ArrayList<Task> getAllTask() throws RepositoryException
    {
        return repository.getAllTask();
    }

    public Task addTask(Task t) throws RepositoryException
    {
        return repository.addTask(t);
    }

    public void removeTask(Task t) throws RepositoryException
    {
        repository.removeTask(t);
    }

    public void modifyTask(Task t) throws RepositoryException
    {
        repository.modifyTask(t);
    }

    public Task comprobar(int identifier) throws RepositoryException
    {
        for(Task t : repository.getAllTask())
        {
            if(t.getIdentifier() == identifier)
            {
                return t;
            }
        }
        return null;
    }
}
