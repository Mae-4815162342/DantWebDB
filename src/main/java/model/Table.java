package model;
import storage.RowStorage;

import java.util.LinkedHashMap;

public class Table {
    private String tableName;
    private LinkedHashMap<String, String> columns;
    private final RowStorage lines;
    
    public Table(String tableName, LinkedHashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new RowStorage();
        this.columns = columns;
    }

    public void insertEntry(String line) {
        Row row =  new Row(line);
        lines.insert(row);
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public RowStorage getLines() {
        return lines;
    }

    public String getTableName() {
        return tableName;
    }

    public void deleteEntry(Row row){
        lines.delete(row);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}