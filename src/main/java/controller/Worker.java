package controller;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import com.google.gson.Gson;
import exception.TableExistsException;
import exception.TableNotExistsException;
import exception.UnknownTypeOfSelect;
import model.*;
import java.util.ArrayList;
// methodes recu par les endpoints create table, insert into table, ...
import java.util.LinkedHashMap;

public class Worker {
    private static Database database;
    private static Worker instance;
    private final Gson gson = new Gson();
    private SelectInterface currentSelect;
    private volatile ArrayList<Object> reqRes;

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

    public Object getColumns(String table) throws Exception {
        return database.getColumns(table);
    }

    public Object select(String tableName) throws Exception {
        if(this.currentSelect == null ) {
            throw new InvalidSelectRequestException();
        }
        return database.select(currentSelect, tableName);
    }

    public void setRequest(String jsonStr, String type, String tableName) throws Exception {
        database.getTableByName(tableName);
        switch(type){
            case "findUnique":
                currentSelect = gson.fromJson(jsonStr, FindUniqueSelect.class);
                break;
            case "findMany":
                currentSelect = gson.fromJson(jsonStr, FindManySelect.class);
                break;
            default:
                throw new UnknownTypeOfSelect(type);
        }
    }

    public Boolean reqHasSelect() {
        return currentSelect != null && currentSelect.hasSelect();
    }

    public Boolean reqHasWhere() {
        return currentSelect != null && currentSelect.hasWhere();
    }

    public Boolean reqHasGroupBy() {
        return currentSelect != null && currentSelect.hasGroupBy();
    }

    public int reqLimit() {
        return currentSelect != null ? currentSelect.getLimit() : -1;
    }

    public void addLinesToAnswer(Object lines) {
        if (reqRes == null) {
            reqRes = new ArrayList<>();
        }
        reqRes.add(lines);
    }

    public Object getRes() {
        return reqRes;
    }

}
