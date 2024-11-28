package model;

public class ExporterException extends Exception
{
    public ExporterException(String mensaje)
    {
        super(mensaje);
    }

    public ExporterException(String mensaje, Throwable cause)
    {
        super(mensaje, cause);
    }
}
