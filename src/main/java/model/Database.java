package model;
import exception.TableExistsException;
import exception.TableNotExistsException;
import model.requests.Aggregate;
import model.requests.FindManySelect;
import model.requests.GroupBy;
import model.requests.SelectSchema;

import java.util.*;

import com.google.gson.Gson;

public class Database {
    private final HashMap<String, Table> tables;
    private final Gson gson = new Gson();

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
            throw new TableExistsException(tableName);

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
            table.insertEntry(entry);
        }
    }

    public Object getColumns(String tableName) throws Exception {
        Table table = this.getTableByName(tableName);
        return table.getColumns();
    }
  
    public void insertChunkIntoTable(String tableName, ArrayList<String> entries) throws TableNotExistsException {
        /* test if the table is in the database */
        Table table = getTableByName(tableName);
        if (table != null) {
            entries.stream().parallel().forEach(line -> {
                if(line!=null){
                    table.insertEntry(line);
                } 
            });

        }
    }
}
