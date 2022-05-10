package exception;

public class ColumnNotExistsException extends Exception {
    public ColumnNotExistsException(){
        super(" Some columns does not exists in this table, please try again");
    }
    public ColumnNotExistsException(String column){
        super(column + " does not exists in this table !");
    }
}

