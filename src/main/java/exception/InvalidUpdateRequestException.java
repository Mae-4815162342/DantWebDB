package exception;

public class InvalidUpdateRequestException extends Exception {
    public InvalidUpdateRequestException(){
        super("Wrong request format, please fill the data and where fields and try again.");
    }
}

