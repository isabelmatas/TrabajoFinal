package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

public class CSVExporter implements IExporter
{
    public void exportarCSV(ArrayList<Task> tareas, String archivo) throws ExporterException
    {
        try
        {
            Path ruta = Path.of(archivo);
            ArrayList <String> lineas = new ArrayList<>();
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-mm-dd");
            for(Task tarea : tareas)
            {
                StringBuilder linea = new StringBuilder();
                linea.append(tarea.getIdentifier()).append(",").append(tarea.getTitle()).append(",").append(fecha.format(tarea.getDate())).append(",")
                .append(tarea.getContent()).append(",").append(tarea.getPriority()).append(",").append(tarea.getEstimatedDuration())
                .append(",").append(archivo).append(tarea.getCompleted());
                lineas.add(linea.toString());
            }
            Files.write(ruta, lineas, StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            throw new ExporterException("Error al exportar CSV");
        }
    }

    public ArrayList<Task> importarCSV(String archivo) throws ExporterException
    {
        ArrayList<Task> tareas = new ArrayList<>();
        try
        {
            Path ruta = Path.of(archivo);
            List <String> lineas = Files.readAllLines(ruta);

            for(String linea : lineas)
            {
                Task tarea = getTareaFromDelimitedString(linea, ",");
                if(tarea != null)
                {
                    tareas.add(tarea);
                }
            }
        }
        catch(IOException e)
        {
            throw new ExporterException("Error al importar CSV");
        }
        return tareas;
    }

    public Task getTareaFromDelimitedString(String delimitedString, String delimitador)
    {
        String[] tasks = delimitedString.split(delimitador);
        if(tasks.length != 7)
        {
            return null;
        }
        try
        {
            int identifier = Integer.parseInt(tasks[0]);
            String title = tasks[1];
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-mm-dd");
            Date date = fecha.parse(tasks[2]);
            String content = tasks[3];
            int priority = Integer.parseInt(tasks[4]);
            int estimatedDuration = Integer.parseInt(tasks[5]);
            boolean completed = Boolean.parseBoolean(tasks[6]);
            return new Task(identifier, title, date, content, priority, estimatedDuration, completed);

        }
        catch(Exception e)
        {
            return null;
        }
    }
}