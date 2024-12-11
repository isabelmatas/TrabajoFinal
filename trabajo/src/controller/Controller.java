package controller;
import view.BaseView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.ExporterException;
import model.ExporterFactory;
import model.IExporter;
import model.Model;
import model.RepositoryException;
import model.Task;

public class Controller
{
    private final Model model;
    private final BaseView view;
    
    public Controller(Model model, BaseView view)
    {
        this.model = model;
        this.view = view;
    }

    public void setExporter(String tipo)
    {
        try
        {
            IExporter exporter = ExporterFactory.crearExporter(tipo);
            model.setExporter(exporter);
            view.showMessage("Exportador configurado correctamente");
        }
        catch(ExporterException e)
        {
            view.showErrorMessage("Error al configurar el exportador: " + e.getMessage());
        }
    }

    public void exportarTareas(String archivo)
    {
        try
        {
            model.exportarTareas(archivo);
            view.showMessage("Tareas exportadas correctamente");
        }
        catch(RepositoryException | ExporterException e)
        {
            view.showErrorMessage("Error al exportar tareas: " + e.getMessage());
        }
    }

    public void importarTareas(String archivo)
    {
        try
        {
            model.importarTareas(archivo);
            view.showMessage("Tareas importadas correctamente");
        }
        catch(RepositoryException | ExporterException e)
        {
            view.showErrorMessage("Error al importar tareas: " + e.getMessage());
        }
    }

    public void iniciarAplicacion()
    {
        try
        {
            model.getAllTask();
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
        return model.getAllTask();
    }

    public Task addTask(Task t) throws RepositoryException
    {
        return model.addTask(t);
    }

    public void removeTask(Task t) throws RepositoryException
    {
        model.removeTask(t);
    }

    public void modifyTask(Task t) throws RepositoryException
    {
        model.modifyTask(t);
    }

    public Task comprobar(int identifier) throws RepositoryException
    {
        for(Task t : model.getAllTask())
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
        ArrayList<Task> tareasCompletas = model.getAllTask();
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

    public void guardarEstado()
    {
        try
        {
            model.guardarEstado();
            view.showMessage("El estado se ha guardado correctamente");
        }
        catch(RepositoryException e)
        {
            view.showErrorMessage("Error al guardar el estado: " + e.getMessage());
        }
    }

    public void finalizarAplicacion()
    {
        try
        {
            model.guardarEstado();
            view.showMessage("El estado se ha guardado correctamente");
        }
        catch(RepositoryException e)
        {
            view.showErrorMessage("Error al guardar el estado: " + e.getMessage());
        }
        view.end();
    }
}
