package storage;

import java.util.ArrayList;
import java.util.HashMap;

public class MaelysStorage {
    public String name;
    public ArrayList<String> columnsNames = new ArrayList<>();
    public ArrayList<String> columnsTypes = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();

    public MaelysStorage(String name, HashMap<String, String> columns) {
        this.name = name;
        for(String column: columns.keySet()) {
            columnsNames.add(column);
            columnsTypes.add(columns.get(column));
        }
    }

    public String toString() {
        String res = "table " + name + "\n{";
        for(int i = 0; i < columnsNames.size(); i++) {
            res += "\t" + columnsNames.get(i) + "(" + columnsTypes.get(i) + ")\n";
        }
        res+="}";
        return res;
    }

    public void insertLine(String line) {
        data.add(line);
    }

    public String getData() {
        String res = name + " data :\n{\n";
        for(String line: data) {
            res+= "\t" + line + "\n";
        }
        res+="}";
        return res;
    }
}
