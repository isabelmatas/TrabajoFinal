package controller;
import view.BaseView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.ExporterException;
import model.ExporterFactory;
import model.IExporter;
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

    public void iniciarAplicacion()
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

    public ArrayList<Task> getTareasIncompletas() throws RepositoryException
    {
        ArrayList<Task> tareasCompletas = repository.getAllTask();
        ArrayList<Task> tareasIncompletas = new ArrayList<>();
        for(Task t : tareasCompletas)
        {
            if(!t.getCompleted())
            {
                tareasIncompletas.add(t);
            }
        }
        Collections.sort(tareasIncompletas, new Comparator<Task>()
        {
            public int compare(Task t1, Task t2)
            {
                return Integer.compare(t2.getPriority(), t1.getPriority());
            }
        });
        return tareasIncompletas;
    }

    public void getTareasCompletas()
    {
        //
    }

    public void exportarTareas(String tipo, String ruta) throws RepositoryException
    {
        try
        {
            IExporter exporter = ExporterFactory.crearExporter(tipo);
            ArrayList<Task> tareas = repository.getAllTask();
            exporter.exportarTareas(tareas, ruta);
        }
        catch (ExporterException e)
        {
            view.showErrorMessage("Error al exportar las tareas: " + e.getMessage());
        }
    }

    public void importarTareas(String tipo, String ruta) throws RepositoryException
    {
        try
        {
            IExporter exporter = ExporterFactory.crearExporter(tipo);
            ArrayList<Task> tareas = exporter.importarTareas(ruta);
            for(Task t : tareas)
            {
                try
                {
                    addTask(t);
                }
                catch(RepositoryException e)
                {
                    view.showErrorMessage("Error al importar la tarea");
                }
            }
        }
        catch(ExporterException e)
        {
            view.showErrorMessage("Error al importar las tareas: " + e.getMessage());
        }
    }

    public void finalizarAplicacion()
    {
        //
    }
}
