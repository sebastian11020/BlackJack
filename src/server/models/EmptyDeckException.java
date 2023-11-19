package server.models;

public class EmptyDeckException extends Exception
{
    public EmptyDeckException(String message)
    {
        super(message);
    }
}