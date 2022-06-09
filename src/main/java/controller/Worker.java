package controller;

import com.google.gson.Gson;
import exception.TableExistsException;
import exception.TableNotExistsException;
import exception.UnknownTypeOfSelect;
import model.Database;
import model.Row;
import model.Table;
import model.requests.*;

import javax.lang.model.type.UnknownTypeException;
import java.util.*;
// methodes recu par les endpoints create table, insert into table, ...


public class Worker {
    private static Database database;
    private static Worker instance;
    private final Gson gson = new Gson();
    private SelectSchema currentSelect;

    public Worker(){
        database = new Database();
    }

    public static Worker getInstance() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }

    public static void createTable(String name, LinkedHashMap<String, String> columns) throws TableExistsException {
        database.addTable(name, columns);
    }

    public static Table getTableByName(String name) throws TableNotExistsException {
        return database.getTableByName(name);
    }

    public void insertIntoTable(String tableName, String entry) throws TableNotExistsException {
        database.insertIntoTable(tableName, entry);
    }

    public void insertChunkIntoTable(String tableName, ArrayList<String> entries) throws TableNotExistsException {
        database.insertChunkIntoTable(tableName, entries);
    }

    public void select() throws Exception {
        currentSelect.run();
    }

    public Object getColumns(String table) throws Exception {
        return database.getColumns(table);
    }

    public void setRequest(String jsonStr, String type, String tableName, boolean fromClient) throws Exception {
        Table table = database.getTableByName(tableName);
        if(fromClient) {
            switch (type) {
                case "findUnique":
                    currentSelect = gson.fromJson(jsonStr, FindUniqueSelect.class);
                    break;
                case "findMany":
                    currentSelect = gson.fromJson(jsonStr, FindManySelect.class);
                    break;
                case "groupBy":
                    currentSelect = gson.fromJson(jsonStr, GroupBy.class);
                    break;
                case "Aggregate":
                    currentSelect = gson.fromJson(jsonStr, Aggregate.class);
                    break;
                default:
                    throw new UnknownTypeOfSelect(type);
            }
        } else {
            switch (type) {
                case "findUnique":
                    currentSelect = gson.fromJson(jsonStr, FindManySelect.class);
                    ((FindManySelect)currentSelect).setLimit(1);
                    break;
                case "groupBy":
                    currentSelect = gson.fromJson(jsonStr, FindManySelect.class);
                    ((FindManySelect)currentSelect).setLimit(-1);
                default:
                    currentSelect = gson.fromJson(jsonStr, FindManySelect.class);
                    break;
            }
        }
        currentSelect.setRequest(table, fromClient);
    }

    public Boolean reqHasSelect() {
        return currentSelect != null && currentSelect.hasSelect();
    }

    public Boolean reqHasWhere() {
        return currentSelect != null && currentSelect.hasWhere();
    }

    public Boolean reqIsGroupBy() {
        return currentSelect != null && currentSelect instanceof GroupBy;
    }

    public Boolean reqIsAggregate() {
        return currentSelect != null && currentSelect instanceof Aggregate;
    }

    public int reqLimit() {
        return currentSelect != null ? currentSelect.getLimit() : -1;
    }

    public Object getRes() {
        return currentSelect.getRes();
    }

    public void addLinesToAnswer(Object lines) throws Exception{
        currentSelect.addLines(lines);
    }
}
