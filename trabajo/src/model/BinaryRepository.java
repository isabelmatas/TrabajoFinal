package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class BinaryRepository implements IRepository
{
    private final String ruta = System.getProperty("user.home") + "/tasks.bin";
    private ArrayList<Task> tareas;

    public BinaryRepository() throws RepositoryException
    {
        cargarTareas();
    }

    public void cargarTareas() throws RepositoryException
    {
        File archivo = new File(ruta);
        if(archivo.exists() && archivo.isFile())
        {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo)))
            {
                tareas = (ArrayList<Task>) ois.readObject();
            }
            catch(IOException | ClassNotFoundException e)
            {
                throw new RepositoryException("Error al cargar las tareas: " +e.getMessage());
            }
        }
        else
        {
            tareas = new ArrayList<>();
        }
    }

    public void guardarTareas() throws RepositoryException
    {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta)))
        {
            oos.writeObject(tareas);
        }
        catch(IOException e)
        {
            throw new RepositoryException("Error al guardar las tareas");
        }
    }

    public Task addTask(Task tarea) throws RepositoryException
    {
        for(Task t : tareas)
        {
            if(t.getIdentifier() == tarea.getIdentifier())
            {
                throw new RepositoryException("Ya existe una tarea con ese id");
            }
        }
        tareas.add(tarea);
        guardarTareas();
        return tarea;
    }

    public void removeTask(Task tarea) throws RepositoryException
    {
        if(!tareas.remove(tarea))
        {
            throw new RepositoryException("La tarea no existe");
        }
        guardarTareas();
    }

    public void modifyTask(Task tarea) throws RepositoryException
    {
        boolean tareaModificada = false;

        for(Task t : tareas)
        {
            if(t.getIdentifier() == tarea.getIdentifier())
            {
                t.setTitle(tarea.getTitle());
                t.setDate(tarea.getDate());
                t.setContent(tarea.getContent());
                t.setPriority(tarea.getPriority());
                t.setEstimatedDuration(tarea.getEstimatedDuration());
                t.setCompleted(tarea.getCompleted());
                tareaModificada = true;
                break;
            }
        }
        if(!tareaModificada)
        {
            throw new RepositoryException("La tarea no se ha encontrado");
        }
        guardarTareas();
    }

    public ArrayList<Task> getAllTask()
    {
        return new ArrayList<>(tareas);
    }
}
