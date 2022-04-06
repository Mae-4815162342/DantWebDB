package database;
import storage.SarahStorage;
import storage.Storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Table {
    private String tableName;
    public ArrayList<String> columnsNames = new ArrayList<>();
    public ArrayList<String> columnsTypes = new ArrayList<>();

    private final Storage lines;

    public Table(String tableName,  HashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new SarahStorage();

        for(Map.Entry<String, String> entry : columns.entrySet()) {
            this.columnsNames.add(entry.getKey();
            this.columnsTypes.add(entry.getValue());
        }

    }

    public void insertEntry(ArrayList<String> columnsMap) {
        Row row =  new Row(columnsMap);
        lines.insert(row);
        System.out.println("Successfully added a row");
    }

    public Storage getlines() {
        return this.lines;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}