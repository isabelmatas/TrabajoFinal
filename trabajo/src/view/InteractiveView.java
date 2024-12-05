package view;
import controller.Controller;
import model.Task;
import static com.coti.tools.Esdia.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class InteractiveView extends BaseView
{
    Scanner sc = new Scanner(System.in);

    public InteractiveView(Controller controlador)
    {
        super(controlador);
    }

    @Override
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


    private void crearTarea()
    {
        try
        {
            // si añado otra tarea con el mismo identificador no deja crearla (BinaryRepository)
            int identifier = readInt("Introduce el identificador: ");
            String title = readString("Introduce el titulo: ");
            
            System.out.print("Introduce la fecha (yyyy-mm-dd): ");
            String fecha = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(fecha); 

            String content = readString("Introduce el contenido: ");

            int priority; 
            do
            {
                priority = readInt("Introduce la prioridad (1-5): ");
                if(priority < 1 || priority > 5)
                {
                    showErrorMessage("Error. La prioridad debe estar entre 1 y 5");
                }
            } while(priority < 1 || priority > 5);

            int estimatedDuration = readInt("Introduce la duracion estimada: ");
            
            System.out.print("Tarea completada? (true/false): ");
            boolean completed = Boolean.parseBoolean(sc.nextLine());

            Task nuevaTarea = new Task(identifier, title, date, content, priority, estimatedDuration, completed);
            controlador.addTask(nuevaTarea);
            showMessage("La tarea se ha creado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al crear la tarea: " + e.getMessage());
        }
    }

    private void eliminarTarea()
    {
        try
        {
            int identifier = readInt("Introduce el id de la tarea a eliminar: ");
            Task tarea = controlador.comprobar(identifier);
            if(tarea == null)
            {
                showMessage("La tarea no se ha encontrado");
            }
            else
            {
                controlador.removeTask(tarea);
                showMessage("La tarea se ha eliminado");
            }
        }
        catch(Exception e)
        {
            showErrorMessage("Error al eliminar la tarea: " + e.getMessage());
        }
    }

    private void modificarTarea()
    {
        try
        {
            int identifier = readInt("Introduce el id de la tarea a modificar: ");
            Task tarea = controlador.comprobar(identifier);
            if(tarea == null)
            {
                showMessage("La tarea no se ha encontrado");
            }
            String title = readString("Introduce el nuevo titulo: ");
            
            System.out.print("Introduce la nueva fecha (yyyy-mm-dd): ");
            String fecha = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = dateFormat.parse(fecha); 

            String content = readString("Introduce el nuevo contenido: ");
            int priority = readInt("Introduce la nueva prioridad (1-5): ");
            int estimatedDuration = readInt("Introduce la nueva duracion: ");

            System.out.print("Tarea completada? (true/false): ");
            boolean completed = Boolean.parseBoolean(sc.nextLine());

            Task nuevaTarea = new Task(identifier, title, date, content, priority, estimatedDuration, completed); 

            controlador.modifyTask(nuevaTarea);
            showMessage("La tarea se ha modificado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al modificar la tarea: " + e.getMessage());
        }
    }

    private void listarTareasIncompletas()
    {
        try
        {
            ArrayList<Task> tareas = controlador.getTareasIncompletas();
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

    private void listarTareas()
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

    private void exportarTareas()
    {
        try
        {
            String tipo = readString("Introduce el tipo del archivo (json/csv): ");
            String ruta = System.getProperty("user.home") + "/output." + tipo;
            controlador.exportarTareas(tipo, ruta);
            showMessage("Las tareas se han exportado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al exportar las tareas: " + e.getMessage());
        }
    }

    private void importarTareas()
    {
        try
        {
            String tipo = readString("Introduce el tipo del archivo (json/csv):");
            String ruta = System.getProperty("user.home") + "/output." + tipo;
            controlador.importarTareas(tipo, ruta);
            showMessage("Las tareas se han importado correctamente");
        }
        catch(Exception e)
        {
            showErrorMessage("Error al importar las tareas: " + e.getMessage());
        }
    }

    public void marcarTarea()
    {
        try
        {
            int identifier = readInt("Introduce el id de la tarea para marcarla como completada/incompletada: ");
            Task tarea = controlador.comprobar(identifier);
            if(tarea == null)
            {
                showMessage("La tarea no se ha encontrado");
            }
            else
            {
                boolean estado = !tarea.getCompleted();
                tarea.setCompleted(estado);
                controlador.modifyTask(tarea);
                showMessage("El estado se ha cambiado correctamente");
            }
        }
        catch(Exception e)
        {
            showErrorMessage("Error al marcar la tarea: " + e.getMessage());
        }
    }

    @Override
    public void showMessage(String mensaje)
    {
        System.out.println(mensaje);
    }

    @Override
    public void showErrorMessage(String mensajeError)
    {
        System.err.println(mensajeError);
    }

    @Override
    public void end()
    {
        showMessage("Saliendo...");
    }
}
