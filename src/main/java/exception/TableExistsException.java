package exception;

public class TableExistsException extends Exception {
    public TableExistsException(String msg){
        super(msg);
    }
}
