package exception;

public class ColumnNotExistsException extends Exception {
    public ColumnNotExistsException(String column){
        super(column + " does not exists in this table !");
    }
}

