package model;
import exception.TableExistsException;
import exception.TableNotExistsException;
import model.requests.Aggregate;
import model.requests.BasicSchema;
import model.requests.FindManySelect;
import model.requests.FindUniqueSelect;
import model.requests.GroupBy;

import java.util.*;

import com.google.gson.Gson;

public class Database {
    private final HashMap<String, TableTree> tables;
    private final Gson gson = new Gson();

    public Database() {
        this.tables = new HashMap<>();
    }

    public TableTree createTable(String tableName, LinkedHashMap<String, String> columns ) {
        if (tables.containsKey(tableName)) {
            System.out.println(tableName + " already exists, please chose another name");
        } else {
            TableTree table = new TableTree(tableName, columns);
            tables.put(tableName, table);
            System.out.println("Table successfully created");
        }
        return tables.get(tableName);
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
        System.out.println("Table successfully dropped");
    }

    public HashMap<String, TableTree> getTables() {
        return this.tables;
    }

    public void showTables() {
        for(Map.Entry<String, TableTree> entry : this.tables.entrySet()) {
            String tableName = entry.getKey();
            System.out.println("--> " + tableName);
        }
    }

    public TableTree getTableByName(String tableName) throws TableNotExistsException {
        TableTree t=this.tables.get(tableName);
        if(t==null) {
            throw new TableNotExistsException(tableName);
        }
        return t;
    }

    public void addTable(String tableName, LinkedHashMap<String, String> columns) throws TableExistsException {
        try{
            /* test if the table is in the database */
            getTableByName(tableName);
            throw new TableExistsException(tableName + " already exists in the database !");

        } catch (TableNotExistsException e) {
            /* the table can be added to database */
            TableTree newTable = new TableTree(tableName, columns);
            tables.put(tableName, newTable);
        }
    }

    public void insertIntoTable(String tableName, String entry) throws TableNotExistsException {
        /* test if the table is in the database */
        TableTree table = getTableByName(tableName);
        if (table != null) {
            table.insertEntry(entry);
        }
    }

    public Object getColumns(String tableName) throws Exception {
        TableTree table = this.getTableByName(tableName);
        return table.getColumns();
    }

    public void insertChunkIntoTable(String tableName, ArrayList<String> entries) throws TableNotExistsException {
        /* test if the table is in the database */
        TableTree table = getTableByName(tableName);
        if (table != null) {
            entries.stream().parallel().forEach(line -> {
                if(line!=null){
                    table.insertEntry(line);
                } 
            });

        }
    }

    public void addTable(TableTree input) throws TableExistsException {
        String tableName=input.getName();
        try{
            /* test if the table is in the database */

            getTableByName(tableName);
            throw new TableExistsException(tableName + " already exists in the database !");

        } catch (TableNotExistsException e) {
            /* the table can be added to database */
            input.initialize();
            tables.put(tableName, input);
            System.out.println(tableName+" inserted");
        }
    }

    public int getSize(String table) throws TableNotExistsException {
        return getTableByName(table).getTableSize();
    }
}
