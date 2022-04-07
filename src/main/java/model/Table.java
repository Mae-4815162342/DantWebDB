package model;
import storage.Storage;
import storage.SarahStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private String tableName;
    public HashMap<String, String> columns;
    private final SarahStorage lines;

    public Table(String tableName, HashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new SarahStorage();
        this.columns = columns;
    }

    public void insertEntry(ArrayList<String> columnsMap) {
        Row row =  new Row(columnsMap);
        lines.insert(row);
        System.out.println("Successfully added a row");
    }

    public HashMap<String, String> getColumns() {
        return columns;
    }

    public SarahStorage getLines() {
        return lines;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}