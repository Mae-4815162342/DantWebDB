package controller;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.InvalidUpdateRequestException;
import exception.TableExistsException;
import exception.TableNotExistsException;
import model.Database;
import model.Table;

// methodes recu par les endpoints create table, insert into table, ...
import java.util.LinkedHashMap;

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

    public static void createTable(String name, LinkedHashMap<String, String> columns) throws TableExistsException{
        database.addTable(name,columns);
    }

    public static Table getTableByName(String name) throws TableNotExistsException {
        return database.getTableByName(name);
    }

    public void insertIntoTable(String line, Table table) throws TableNotExistsException {
        database.insertIntoTable(line, table);
    }
    public Object select(String jsonStr, String type, String table) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
        return database.select(jsonStr, type, table);
    }

    public Object update(String jsonStr, String type, String table) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
      return database.update(jsonStr, type, table);
    }

    public Object delete(String jsonStr, String type, String table) throws TableNotExistsException, ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException {
        return database.delete(jsonStr, type, table);
    }
}
