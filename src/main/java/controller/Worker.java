package controller;

import model.Database;

public class Worker {
    private Database database;
    private static Worker instance = new Worker();

    public Worker(){
        this.database = new Database("DB");
    }

    public static Worker getWorker() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }

    // methodes recu par les endpoints create table, insert into table, ...
}
