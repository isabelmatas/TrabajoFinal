package model;
import java.util.ArrayList;

public class Model
{
    private IRepository repository;
    private IExporter exporter;

    public Model(IRepository repository, IExporter exporter)
    {
        this.repository = repository;
        this.exporter = exporter;
    }

    public Task addTask(Task tarea) throws RepositoryException
    {
        return repository.addTask(tarea);
    }

    public void removeTask(Task tarea) throws RepositoryException
    {
        repository.removeTask(tarea);
    }

    public void modifyTask(Task tarea) throws RepositoryException
    {
        repository.modifyTask(tarea);
    }

    public ArrayList<Task> getAllTask() throws RepositoryException
    {
        return repository.getAllTask();
    }

    public void exportarTareas(String archivo) throws RepositoryException, ExporterException
    {
        if(exporter == null)
        {
            throw new ExporterException("El exportar no se ha configurado");
        }
        exporter.exportarTareas(repository.getAllTask(), archivo);
    }

    public void importarTareas(String archivo) throws RepositoryException, ExporterException
    {
        if(exporter == null)
        {
            throw new ExporterException("El exportador no se ha configurado");
        }
        ArrayList<Task> tareasImportadas = exporter.importarTareas(archivo);
        for(Task tarea : tareasImportadas)
        {
            try
            {
                addTask(tarea);
            }
            catch(RepositoryException e)
            {

            }
        }
    }

    public void setRepository(IRepository repository)
    {
        this.repository = repository;
    }

    public void setExporter(IExporter exporter)
    {
        this.exporter = exporter;
    }
}
