package model;
import exception.TableExistsException;
import exception.TableNotExistsException;

import java.util.*;

import com.google.gson.Gson;

public class Database {
    private final HashMap<String, Table> tables;

    public Database() {
        this.tables = new HashMap<>();
    }

    public Table createTable(String tableName, LinkedHashMap<String, String> columns ) {
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

    public Table getTableByName(String tableName) throws TableNotExistsException {
        for(Map.Entry<String, Table> entry : this.tables.entrySet()) {
            String name = entry.getKey();
            if(name.equals(tableName)){
                return entry.getValue();
            }
        }
        throw  new TableNotExistsException(tableName);
    }

    public void addTable(String tableName, LinkedHashMap<String, String> columns) throws TableExistsException {
        try{
            /* test if the table is in the database */
            getTableByName(tableName);
            throw new TableExistsException(tableName + " already exists in the database !");

        } catch (TableNotExistsException e) {
            /* the table can be added to database */
            Table newTable = new Table(tableName, columns);
            tables.put(tableName, newTable);
        }
    }

    public void insertIntoTable(String tableName, String entry) throws TableNotExistsException {
        /* test if the table is in the database */
        Table table = getTableByName(tableName);
        if (table != null) {
            ArrayList<String> row = new ArrayList<>(Arrays.asList(entry.split(",")));
            /* the entry can be added to the table */
            table.insertEntry(row);
        }
    }

    public Object getColumns(String tableName) throws Exception {
        Table table = this.getTableByName(tableName);
        return table.getColumns();
    }


    public Object select(SelectInterface request, String tableName) throws Exception {
        Table table = getTableByName(tableName);
        Object res = null;
        try {
            res = request.run(table);
        } catch (Exception e) {
            throw e;
        }
        return res;
    }

    public void insertChunkIntoTable(String tableName, ArrayList<String> entries) throws TableNotExistsException {
        /* test if the table is in the database */
        Table table = getTableByName(tableName);
        if (table != null) {
            entries.stream().parallel().forEach(line -> {
                if(line!=null){
                    ArrayList<String> entry = new ArrayList<>(Arrays.asList(line.split(",")));
                    /* the entry can be added to the table */
                    table.insertEntry(entry);
                } 
            });

        }
    }
}
