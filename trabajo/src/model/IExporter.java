package model;
import java.rmi.server.ExportException;
import java.util.ArrayList;

public interface IExporter
{
    void exportarTareas(ArrayList<Task> tareas, String archivo) throws ExporterException;
    ArrayList<Task> importarTareas(String archivo) throws ExporterException;
} 