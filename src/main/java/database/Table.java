package database;
import storage.SarahStorage;
import storage.Storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Table {
    private String tableName;

    private final Storage rows;

    public Table(String tableName) {
        this.tableName = tableName;
        this.rows = new SarahStorage();
    }

    public void insertEntry(ArrayList<String> columnsMap) {
        Row row =  new Row(columnsMap);
        rows.insert(row);
        System.out.println("Successfully added a row");
    }

    public Storage getRows() {
        return this.rows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}