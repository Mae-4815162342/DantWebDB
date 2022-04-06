package model;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Structure {
    public static ArrayList<ClientTable> tables = new ArrayList<>();

    public static Boolean createTable(String name, ArrayList<Tuple> columns) {
        tables.add(new ClientTable(name, columns));
        return true;
    }

    public static  ArrayList<ClientTable> getTables() {
        return tables;
    }

    public static String showTables() {
        String res = "";
        for (ClientTable table: tables) {
            res += table.toString() + ",";
        }
        return res;
    }

    public static ClientTable getTableByName(String tableName) {
        for(ClientTable table : tables) {
            if(table.name.equals(tableName)) {
                return table;
            }
        }
        return null;
    }
}
