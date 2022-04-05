package database;
import java.util.Date;
import java.util.HashMap;

public class Row {
    private String rowId;
    private HashMap<String, String> columnValuesMap;

    public Row(String rowId, HashMap<String, String> columnsMap) {
        this.rowId = rowId;
        this.columnValuesMap = columnsMap;
    }

    public HashMap<String, String> getColumnValuesMap() {
        return columnValuesMap;
    }

    public void setColumnValuesMap(HashMap<String, String> columnValuesMap) {
        this.columnValuesMap = columnValuesMap;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
}
