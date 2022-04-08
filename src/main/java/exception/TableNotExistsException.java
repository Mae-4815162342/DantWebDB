package exception;

public class TableNotExistsException extends Exception {
    public TableNotExistsException(String tableName){
        super(tableName + " does not exists in the database !");
    }
}

