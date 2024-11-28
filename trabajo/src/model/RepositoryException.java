package model;

public class RepositoryException extends Exception
{
    public RepositoryException(String mensaje)
    {
        super(mensaje);
    }

    public RepositoryException(String mensaje, Throwable cause)
    {
        super(mensaje, cause);
    }
}
