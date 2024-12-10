package model;
import java.util.ArrayList;

public interface IRepository
{
    Task addTask(Task t) throws RepositoryException;
    void removeTask(Task t) throws RepositoryException;
    void modifyTask(Task t) throws RepositoryException;
    ArrayList<Task> getAllTask() throws RepositoryException;
    void setExporter(IExporter exporter);
    void exportarTareas(String archivo) throws RepositoryException, ExporterException;
    void importarTareas(String archivo) throws RepositoryException, ExporterException;
    void guardarEstado() throws RepositoryException;
}
