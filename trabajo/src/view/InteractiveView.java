package view;
import controller.Controller;
import model.Task;
import static com.coti.tools.Esdia.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;

public class InteractiveView extends BaseView
{
    public InteractiveView(Controller controlador)
    {
        super(controlador);
    }

    public void init()
    {
        int opcion;
        do
        {
            System.out.println("MENU CRUD");
            System.out.println("1. Crear tarea");
            System.out.println("2. Listar tareas incompletas ordenadas por prioridad");
            System.out.println("3. Listar todas las tareas"); // completadas o no
            System.out.println("4. Marcar tarea como completa o incompleta");
            System.out.println("5. Modificar tarea");
            System.out.println("6. Eliminar tarea");
            System.out.println("7. Exportar tareas (JSON/CSV)");
            System.out.println("8. Importar tareas (JSON/CSV)");
            System.out.println("9. Volver al menu principal");
            opcion = readInt("Introduce una opcion: ");
            
            switch(opcion)
            {
                case 1:
                    crearTarea();
                    break;
                case 2:
                    listarTareasIncompletas();
                    break;
                case 3:
                    listarTareas();
                    break;
                case 4:
                    marcarTarea();
                    break;
                case 5:
                    modificarTarea();
                    break;
                case 6:
                    eliminarTarea();
                    break;
                case 7:
                    exportarTareas();
                    break;
                case 8:
                    importarTareas();
                    break;
                case 9:
                    end();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        } while(opcion != 9);
    }

    public void crearTarea()
    {
        try
        {
            int identifier = readInt("Introduce el identificador: ");
            String titulo = readString("Introduce el titulo: ");
            Date date = readDate("Introduce la fecha: "); // ??
            String content = readString("Introduce el contenido: ");
            int priority = readInt("Introduce la prioridad (1-5): ");
            int estimatedDuration = readInt("Introduce la duracion estimada: ");
            boolean completed = readBoolean("Esta completada?: "); // ??
            controlador.addTask(identifier, titulo, date, content, priority, estimatedDuration, completed); // por hacer
            showMessage("La tarea se ha creado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al crear la tarea: " + e.getMessage());
        }
    }

    public void eliminarTarea()
    {
        try
        {
            int identifier = readInt("Introduce el id de la tarea a eliminar: ");
            controlador.eliminarTarea(identifier); // por hacer
            showMessage("La tarea se ha eliminado");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al eliminar la tarea: " + e.getMessage());
        }
    }

    public void modificarTarea()
    {
        try
        {
            int identifier = readInt("Introduce el id de la tarea a modificar: ");
        }
    }

    public void listarTareasIncompletas()
    {
        try
        {
            ArrayList<Task> tareas = controlador.getTareasIncompletas(); // por hacer
            if(tareas.isEmpty())
            {
                showMessage("No hay tareas incompletas");
            }
            else
            {
                for(Task t : tareas)
                {
                    System.out.println(t);
                }
            }
        }
        catch(Exception e)
        {
            showErrorMessage("Error al listar las tareas: " + e.getMessage());
        }
    }

    public void listarTareas()
    {
        try
        {
            ArrayList<Task> tareas = controlador.getAllTask();
            if(tareas.isEmpty())
            {
                showMessage("No hay tareas disponibles");
            }
            else
            {
                for(Task t : tareas)
                {
                    System.out.println(t);
                }
            }
        }
        catch(Exception e)
        {
            showErrorMessage("Error al listar las tareas: " + e.getMessage());
        } 
    }



    public void exportarTareas()
    {
        try
        {
            String tipo = readString("Introduce el tipo del archivo (json/csv):");
            if(tipo == "json")
            {
                String ruta = System.getProperty("user.home") + "output.json";
            }
            else if(tipo == "csv")
            {
                String ruta = System.getProperty("user.home") + "output.csv";
            }
            controlador.exportarTareas(tipo, ruta); // por hacer
            showMessage("Las tareas se han exportado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al exportar las tareas: " + e.getMessage());
        }
    }

    public void importarTareas()
    {
        try
        {
            String tipo = readString("Introduce el tipo del archivo (json/csv)");
            if(tipo == "json")
            {
                String ruta = System.getProperty("user.home") + "output.json";
            }
            else if(tipo == "csv")
            {
                String ruta = System.getProperty("user.home") + "output.csv";
            }
            controlador.importarTareas(); // por hacer
            showMessage("Las tareas se han importado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al importar las tareas: " + e.getMessage());
        }
    }


    public void showMessage(String mensaje)
    {
        System.out.println(mensaje);
    }

    public void showErrorMessage(String mensajeError)
    {
        System.err.println(mensajeError);
    }

    public void end()
    {
        showMessage("Saliendo...");
    }
}
