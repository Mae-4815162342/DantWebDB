package model;
import java.util.HashMap;

import storage.SarahStorage;

public class Database {
    private String name;
    private final static HashMap<String, Table> tables = new HashMap<>();

    public Database(String name) {
        this.name = name;
    }

    public Table createTable(String tableName) {
        if (tables.containsKey(tableName)) {
            System.out.println(tableName + " already exists, please chose another name");
        } else {
            Table table = new Table(tableName, null);
            tables.put(tableName, table);
            System.out.println("Table successfully created");
        }
        return tables.get(tableName);
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
        System.out.println("Table successfully dropped");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }
    public static String showTables() {
        String res = "";
        for (Table table : tables.values()) {
            res += table.toString() + ",";
        }
        return res;
    }

    public static SarahStorage getTableByName(String tableName) {
        for(String name : tables.keySet()) {
            if(name.equals(tableName)) {
                return tables.get(name).getlines();
            }
        }
        return null;
    }
    
}
