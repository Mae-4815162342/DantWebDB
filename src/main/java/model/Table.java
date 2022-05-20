package model;
import storage.RowStorage;

import java.util.ArrayList;
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

    public void insertEntry(ArrayList<String> columnsMap) {
        Row row =  new Row(columnsMap);
        lines.insert(row);
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public RowStorage getLines() {
        return lines;
    }

    public int getTableSize() {
        return lines.size();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}