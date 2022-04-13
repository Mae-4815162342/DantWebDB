package exception;

public class WrongParametersException extends Exception{
    public WrongParametersException(String request) { super ("Unable to recognise parameters from request \"" + request + "\""); }
}
