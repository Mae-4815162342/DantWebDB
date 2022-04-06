package database;
import java.util.HashMap;

public class Database {
    private String name;
    private final HashMap<String, Table> tables;

    public Database(String name) {
        this.name = name;
        this.tables = new HashMap<>();
    }

    public Table createTable(String tableName) {
        if (tables.containsKey(tableName)) {
            System.out.println(tableName + " already exists, please chose another name");
        } else {
            Table table = new Table(tableName);
            tables.put(tableName, table);
            System.out.println("Table successfully created");
        }
        return tables.get(tableName);
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
        System.out.println("Table successfully dropped");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public void setTables(HashMap<String, Table> tables) {
        this.tables = tables;
    }

    public static  ArrayList<MaelysStorage> getTables() {
        return tables;
    }

    public static String showTables() {
        String res = "";
        for (MaelysStorage table: tables) {
            res += table.toString() + ",";
        }
        return res;
    }

    public static MaelysStorage getTableByName(String tableName) {
        for(MaelysStorage table : tables) {
            if(table.name.equals(tableName)) {
                return table;
            }
        }
        return null;
    }
    
}
