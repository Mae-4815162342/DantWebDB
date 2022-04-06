package model;

import java.util.ArrayList;

public class MaelysStorage {
    public String name;
    public ArrayList<String> columnsNames = new ArrayList<>();
    public ArrayList<String> columnsTypes = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();

    public ClientTable(String name, HashMap<String, String> columns) {
        this.name = name;
        for(Tuple column: columns) {
            columnsNames.add((String)column.first);
            columnsTypes.add((String)column.second);
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
