package model;
import java.util.ArrayList;

public interface IRepository
{
    Task addTask(Task t) throws RepositoryException;
    void removeTask(Task t) throws RepositoryException;
    void modifyTask(Task t) throws RepositoryException;
    ArrayList<Task> getAllTask() throws RepositoryException;
}
