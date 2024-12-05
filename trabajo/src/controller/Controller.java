package controller;
import view.BaseView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.BinaryRepository;
import model.ExporterException;
import model.ExporterFactory;
import model.IExporter;
import model.IRepository;
import model.NotionRepository;
import model.RepositoryException;
import model.Task;

public class Controller
{
    private final IRepository repository;
    private final BaseView view;
    
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

    public void exportarTareas(String tipo, String ruta) throws RepositoryException
    {
        try
        {
            IExporter exporter = ExporterFactory.crearExporter(tipo);
            ArrayList<Task> tareas = repository.getAllTask();
            exporter.exportarTareas(tareas, ruta);
            view.showMessage("Las tareas se han exportado correctamente");
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
            view.showMessage("Las tareas se han importado correctamente");
        }
        catch(ExporterException e)
        {
            view.showErrorMessage("Error al importar las tareas: " + e.getMessage());
        }
    }

    public void finalizarAplicacion()
    {
        try
        {
            if (repository instanceof BinaryRepository)
            {
                BinaryRepository binRepository = (BinaryRepository) repository;
                binRepository.guardarTareas();
                view.showMessage("El estado se ha guardado correctamente en el archivo binario");
            }
            else if (repository instanceof NotionRepository)
            {
                view.showMessage("El estado se ha guardado correctamente en Notion");
            }
        }
        catch(RepositoryException e)
        {
            view.showErrorMessage("Error al guardar el estado");
        }

        view.end();
    }
}
