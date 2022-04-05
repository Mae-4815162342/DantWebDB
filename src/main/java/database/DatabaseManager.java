package database;

import java.util.HashMap;

public class DatabaseManager {
    private HashMap<String, Database> databaseHashMap;

    public Database createDatabase(String databaseName) {
        if (databaseHashMap.containsKey(databaseName)) {
            System.out.println("A database already exists with this name");
        } else {
            databaseHashMap.put(databaseName, new Database(databaseName));
        }
        return databaseHashMap.get(databaseName);
    }

    public void deleteDatabase(String databaseName) {
        databaseHashMap.remove(databaseName);
    }

    public DatabaseManager() {
        this.databaseHashMap = new HashMap<>();
    }

    public HashMap<String, Database> getDatabaseHashMap() {
        return databaseHashMap;
    }

    public void setDatabaseHashMap(HashMap<String, Database> databaseHashMap) {
        this.databaseHashMap = databaseHashMap;
    }
}