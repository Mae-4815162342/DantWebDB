package model;
import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.InvalidUpdateRequestException;
import exception.TableExistsException;
import exception.TableNotExistsException;
import model.requests.FindManySelect;
import model.requests.FindUniqueSelect;
import model.requests.GroupBy;
import model.requests.Update;
import model.requests.UpdateMany;
import model.requests.Aggregate;
import model.requests.BasicSchema;
import model.requests.Delete;
import model.requests.DeleteMany;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public void insertIntoTable(String entry, Table table ) throws TableNotExistsException {
        /* test if the table is in the database */
        if(table != null) {
            /* the entry can be added to the table */
            table.insertEntry(entry);
        }
    }

    public Object getColumns(String tableName) throws Exception {
        Table table = this.getTableByName(tableName);
        return table.getColumns();
    }

<<<<<<< Updated upstream
    public Object select(String jsonStr, String type, String tableName) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
        Table table = this.getTableByName(tableName);
        BasicSchema select;
        switch(type){
            case "findUnique":
                select = gson.fromJson(jsonStr, FindUniqueSelect.class);
                break;
            case "findMany":
                select = gson.fromJson(jsonStr, FindManySelect.class);
                break;
            case "Aggregate":
                select = gson.fromJson(jsonStr, Aggregate.class);
                break;
            case "groupBy":
                select = gson.fromJson(jsonStr, GroupBy.class);
                break;
            default:
                return null;
        }
=======
    public Object select(SelectInterface request, String tableName) throws Exception {
        Table table = getTableByName(tableName);
        Object res = null;
>>>>>>> Stashed changes
        try {
            res = request.run(table);
        } catch (Exception e) {
            throw e;
        }
        return res;
    }

    public Object update(String jsonStr, String type, String tableName) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
        Table table = this.getTableByName(tableName);
        BasicSchema update;
        switch(type){
            case "updateUnique":
                update = gson.fromJson(jsonStr, Update.class);
                break;
            case "updateMany":
                update = gson.fromJson(jsonStr, UpdateMany.class);
                break;
            default:
                return null;
        }
        Object res = update.run(table);
        return res;
    }
    public Object delete(String jsonStr, String type, String tableName) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
        Table table = this.getTableByName(tableName);
        BasicSchema delete;
        switch(type){
            case "deleteUnique":
            delete = gson.fromJson(jsonStr, Delete.class);
                break;
            case "deleteMany":
            delete = gson.fromJson(jsonStr, DeleteMany.class);
                break;
            default:
                return null;
        }
        Object res = delete.run(table);
        return res;
    }
}
