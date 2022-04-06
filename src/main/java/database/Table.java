package database;
import storage.Storage;

import java.util.Date;
import java.util.HashMap;

public class Table {
    private String tableName;
    private HashMap<String, Row> rows;

    public Table(String tableName) {
        this.tableName = tableName;
        this.rows = new HashMap<>();
    }

    public void insertEntry(String rowId, HashMap<String, String> columnsMap) {
        Row row =  new Row(rowId, columnsMap);
        rows.put(rowId, row);
        System.out.println("Successfully added a row");
    }

    public HashMap<String, Row> getRows() {
        return rows;
    }

    public void setRows(HashMap<String, Row> rows) {
        this.rows = rows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}