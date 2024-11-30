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

    public void iniciar()
    {
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
}
