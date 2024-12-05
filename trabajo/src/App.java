import controller.Controller;
import view.BaseView;
import view.InteractiveView;
import model.BinaryRepository;
import model.IRepository;
import model.NotionRepository;
import model.RepositoryException;

public class App
{
    public static void main(String[] args)
    {
        try
        {
            IRepository repository;
            if(args.length > 0 && args[0].equals("notion"))
            {
                if(args.length < 3)
                {
                    System.out.println("Error al introducir los argumentos");
                    return;
                }
                String apiKey = args[1];
                String databaseId = args[2];
                repository = new NotionRepository(apiKey, databaseId);
            }
            else
            {
                repository = new BinaryRepository();
            }
            BaseView view = new InteractiveView(null);
            Controller controller = new Controller(repository, view);
            view.controlador = controller;
            controller.iniciarAplicacion();
        }
        catch(RepositoryException e)
        {
            System.out.println("Error: "+ e.getMessage());
        }
    }
}
