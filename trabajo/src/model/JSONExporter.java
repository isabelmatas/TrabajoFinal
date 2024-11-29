package model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONExporter implements IExporter
{
    public void exportarTareas(ArrayList<Task> tareas, String archivo) throws ExporterException
    {
        Path ruta = Path.of(archivo);
        Gson gson = new Gson();
        String json = gson.toJson(tareas);
        try
        {
            Files.write(ruta, json.getBytes(StandardCharsets.UTF_8));
        }
        catch(IOException e)
        {
            throw new ExporterException("Error al exportar JSON");
        }
    }

    public ArrayList<Task> importarTareas(String archivo) throws ExporterException
    {
        Path ruta = Path.of(archivo);
        Gson gson = new Gson();
        try (Scanner scannerRef = new Scanner(ruta))
        {
            StringBuilder json = new StringBuilder();
            while (scannerRef.hasNext())
            {
                json.append(scannerRef.nextLine());
            }
            return gson.fromJson(json.toString(), new TypeToken<List<Task>>(){}.getType());
        }
        catch (IOException e)
        {
            throw new ExporterException("Error al importar JSON");
        }
    }
}
