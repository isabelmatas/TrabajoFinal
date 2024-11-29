package model;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BinaryRepository implements IRepository
{
    private final String ruta = System.getProperty("user.home") + "tasks.bin";
    private ArrayList<Task> tareas;

    public BinaryRepository() throws RepositoryException
    {
        //
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
}
