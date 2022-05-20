package model;
import storage.RowStorage;
import storage.Rows.*;

import java.util.*;

public class Table {
    private String tableName;
    private LinkedHashMap<String, Tree> headers=new LinkedHashMap<>();
    private final RowObjectStorage lines;
    private ArrayList<Class> types=new ArrayList<>();
    private int nb;




    public Table(String tableName, LinkedHashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new RowObjectStorage();
        this.nb=0;
        //this.columns = columns;
        Set<String> keys = columns.keySet();

        // printing the elements of LinkedHashMap
        for (String key : keys) {
            String v=columns.get(key);
            System.out.println(key + " -- " + v);
            switch (v) {
                case "int":
                    headers.put(v, new IntTree());
                    types.add(Integer.class);
                case "string":
                    headers.put(v, new IntTree());
                    types.add(Integer.class);

            }
        }
    }


    public void insertEntry(String line) {
        String[] tab=line.split(",");
        
        RowObject row =  new RowObject(null);
        lines.insert(row);
    }

   /* public LinkedHashMap<String, String> getColumns() {
        return columns;
    }*/

    public RowObjectStorage getLines() {
        return lines;
    }

    public String getTableName() {
        return tableName;
    }

    /*public void deleteEntry(RowObject row){
        lines.delete(row);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }*/
}