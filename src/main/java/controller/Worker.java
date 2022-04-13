package controller;

import exception.TableExistsException;
import exception.TableNotExistsException;
import model.Database;
import model.Row;
import model.Table;

import java.util.ArrayList;
import java.util.HashMap;
// methodes recu par les endpoints create table, insert into table, ...

public class Worker {
    private static Database database;
    private static Worker instance;

    public Worker(){
        database = new Database();
    }

    public static Worker getInstance() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }

    public static void createTable(String name, HashMap<String, String> columns) throws TableExistsException {
        database.addTable(name,columns);
    }

    public static Table getTableByName(String name) throws TableNotExistsException {
        return database.getTableByName(name);
    }

    public void insertIntoTable(String tableName, ArrayList<String> entry) throws TableNotExistsException {
        database.insertIntoTable(tableName, entry);
    }
    public String select(String jsonStr, String type, String table) {
        return database.select(jsonStr, type, table);
    }
}
