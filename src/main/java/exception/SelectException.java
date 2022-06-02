package exception;

public class SelectException extends Exception {
    public SelectException(String request) {
        super("Bad request:\n" + request);
    }
}