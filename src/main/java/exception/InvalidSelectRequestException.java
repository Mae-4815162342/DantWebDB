package exception;

public class InvalidSelectRequestException extends Exception {
    public InvalidSelectRequestException(){
        super("Wrong request format, please try again.");
    }
}

