package exception;

public class TableExistsException extends Exception {
    public TableExistsException(String tableName){
        super(tableName + " already exists in the database !");
    }
}
