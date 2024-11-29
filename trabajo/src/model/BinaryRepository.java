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
    private final String ruta = System.getProperty("user.home") + "tasks.bin";
    private ArrayList<Task> tareas;

    public BinaryRepository() throws RepositoryException
    {
        //
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
            catch(IOException e)
            {
                throw new RepositoryException("Error al cargar las tareas");
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

    public Task agregarTarea(Task tarea) throws RepositoryException
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

    public void eliminarTarea(Task tarea) throws RepositoryException
    {
        if(!tareas.remove(tarea))
        {
            throw new RepositoryException("La tarea no existe");
        }
        guardarTareas();
    }
}
