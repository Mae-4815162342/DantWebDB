package model;
import exception.TableExistsException;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final HashMap<String, Table> tables;

    public Database() {
        this.tables = new HashMap<>();
    }

    public Table createTable(String tableName, HashMap<String, String> columns ) {
        if (tables.containsKey(tableName)) {
            System.out.println(tableName + " already exists, please chose another name");
        } else {
            Table table = new Table(tableName, columns);
            tables.put(tableName, table);
            System.out.println("Table successfully created");
        }
        return tables.get(tableName);
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
        System.out.println("Table successfully dropped");
    }

    public HashMap<String, Table> getTables() {
        return this.tables;
    }

    public void showTables() {
        for(Map.Entry<String, Table> entry : this.tables.entrySet()) {
            String tableName = entry.getKey();
            System.out.println("--> " + tableName);
        }
    }

    public Table getTableByName(String tableName) {
        for(Map.Entry<String, Table> entry : this.tables.entrySet()) {
            String name = entry.getKey();
            if(name.equals(tableName)){
                return entry.getValue();
            }
        }
        return null;
    }

    public void addTable(String tableName, HashMap<String, String> columns) throws TableExistsException {
        Table tableExists = getTableByName(tableName);

        if (tableExists != null) {
            throw new TableExistsException(tableName + " already exists in the database !");
        } else {
            Table newTable = new Table(tableName, columns);
            tables.put(tableName, newTable);
        }
    }
}
