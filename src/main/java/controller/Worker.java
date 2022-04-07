package controller;

import model.Database;
import java.util.HashMap;

public class Worker {
    private final Database database;
    private static Worker instance;

    public Worker(){
        this.database = new Database();
    }

    public static Worker getInstance() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }


    public void createTable(String name,  HashMap<String, String> columns){
        database.addTable(name,columns);
        /* try catch pour remonter des erreurs */
    }

    // methodes recu par les endpoints create table, insert into table, ...
}
