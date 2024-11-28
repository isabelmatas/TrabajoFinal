package view;
import controller.Controller;
import static com.coti.tools.Esdia.*;

public class InteractiveView extends BaseView
{
    public void init()
    {
        int opcion;
        do
        {
            System.out.println("MENU CRUD");
            System.out.println("1. Dar de alta");
            System.out.println("2. Listar tareas ordenadas por prioridad y que estan sin completar");
            System.out.println("3. Listado del historial completo de tareas"); // completadas o no
            System.out.println("4. Marcar como completa o incompleta");
            System.out.println("5. Modificar informacion");
            System.out.println("6. Eliminar la tarea");
            System.out.println("7. Exportar CSV");
            System.out.println("8. Importar CSV");
            System.out.println("9. Exportar JSON");
            System.out.println("10. Importar JSON");
            System.out.println("11. Volver al menu principal");
            opcion = readInt("Introduce una opcion: ");
            
            switch(opcion)
            {
                case 1:
                    darAlta();
                    break;
                case 2:
                    listarTareasOrdenadas();
                    break;
                case 3:
                    listarHistorial();
                    break;
                case 4:
                    marcar();
                    break;
                case 5:
                    modificar();
                    break;
                case 6:
                    eliminar();
                    break;
                case 7:
                    exportarCSV();
                    break;
                case 8:
                    importarCSV();
                    break;
                case 9:
                    exportarJSON();
                    break;
                case 10:
                    importarJSON();
                    break;
                case 11:
                
            }
        } while(opcion != 7);
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
