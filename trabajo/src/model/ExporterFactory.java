package model;

public class ExporterFactory 
{
    public static IExporter crearExporter(String tipo) throws ExporterException
    {
        switch (tipo)
        {
            case "csv":
                return new CSVExporter();
            case "json":
                return new JSONExporter();
            default:
                throw new ExporterException("Error, tiene que ser 'csv' o 'json'");
        }
    }
}
