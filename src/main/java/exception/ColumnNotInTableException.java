package exception;

public class ColumnNotInTableException extends Exception{
    public ColumnNotInTableException(String tableName, String column) {
        super("\"" + column + "\" is not a column of " + tableName + "!");
    }
}
