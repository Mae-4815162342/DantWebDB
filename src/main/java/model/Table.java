package model;
import storage.RowStorage;
import storage.Rows.RowObject;
import storage.Rows.RowObjectStorage;
import storage.Rows.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Table {
    private String tableName;
    private HashMap<String,Type> headers=new HashMap<>();
    private final RowObjectStorage lines;
    private ArrayList<String> columnName=new ArrayList<>();
    private ArrayList<Class> types=new ArrayList<>();



    public Table(String tableName, LinkedHashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new RowObjectStorage();
        this.columns = columns;

    }
    public void insertEntry(String line) {
        RowObject row =  new RowObject(null);
        lines.insert(row);
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public RowObjectStorage getLines() {
        return lines;
    }

    public String getTableName() {
        return tableName;
    }

    /*public void deleteEntry(RowObject row){
        lines.delete(row);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }*/
}