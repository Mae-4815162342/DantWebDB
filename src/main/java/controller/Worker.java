package controller;

import exception.TableExistsException;
import model.Database;
import java.util.HashMap;

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
        /* try catch pour remonter des erreurs */
    }

    // methodes recu par les endpoints create table, insert into table, ...
}
