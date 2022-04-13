package model;
import exception.ColumnNotInTableException;
import exception.TableExistsException;
import exception.TableNotExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Table getTableByName(String tableName) throws TableNotExistsException {
        for(Map.Entry<String, Table> entry : this.tables.entrySet()) {
            String name = entry.getKey();
            if(name.equals(tableName)){
                return entry.getValue();
            }
        }
        throw  new TableNotExistsException(tableName);
    }

    public void addTable(String tableName, HashMap<String, String> columns) throws TableExistsException {
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

    public void insertIntoTable(String tableName, ArrayList<String> entry) throws TableNotExistsException {
        /* test if the table is in the database */
        Table table = getTableByName(tableName);
        if(table != null) {
            /* the entry can be added to the table */
            table.insertEntry(entry);
        }
    }

    public ArrayList<String> getRowsWhereColumnEqualsValue(String tableName, String column, String value) throws TableNotExistsException, ColumnNotInTableException {
        Table table = getTableByName(tableName);
        /*get column index in table metradata*/
        int columnIndex = table.getColumnIndex(column);
        /*selecting table rows with params*/
        return table.getRowsWithColumnEquals(columnIndex, value);
    }
}
