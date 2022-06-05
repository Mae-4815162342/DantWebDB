package model;

import storage.RowStorage;

import java.util.*;
import java.util.stream.IntStream;

public class TableTree {
    private String tableName;
    private ArrayList<String[]> rows;
    private LinkedHashMap<String, String> columns;
    private int len=0;
    private ArrayList<ColumnTree> values;


    public TableTree(String name, LinkedHashMap<String, String> columnsEntry){
        this.tableName=name;
        this.columns=columnsEntry;
        this.rows=new ArrayList<>();
        this.values=new ArrayList<>();
        for(Map.Entry<String,String> c:columnsEntry.entrySet()){
            values.add(new ColumnTree(c.getValue()));
        }
        System.out.println(this.tableName+" created");
    }



    public String getName() {
        return this.tableName;
    }

    public void insertEntry(String line) {
        String[]tokens = line.split(";|,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)*(?=(?:[^;]*\"[^;]*\")*[^;]*$)");
        String[] row= new String[tokens.length];

        //int len= rows.size()-1;
        int clen=columns.size();
        /*IntStream.range(0, clen).parallel().forEach(i -> {
            values.get(i).insert(tokens[i],len);
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/

        //System.out.println(len);
        for(int i=0;i<columns.size();i++){
            row[i]=values.get(i).insert(tokens[i],len);
            //System.out.println(len+" "+tokens[i]+" inserted ");

        }
        //this.len++;
        rows.add(row);
        //System.out.println(len);
        if(len==1000000){
            System.out.println(len);
        }
    }

    public int getTableSize() {
        return rows.size();
    }
    public List<String[]> getLines() {
        return rows;
    }
    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }
    public void deleteEntry(Row row){
    }

    public void initialize() {
        this.values=new ArrayList<>();
        this.rows=new ArrayList<>();
        for(Map.Entry<String,String> c:this.columns.entrySet()){
            values.add(new ColumnTree(c.getValue()));
        }
        System.out.println(this.tableName+" created");
    }

    public String getall() {
        String res="Number\tof\trow :" +rows.size()+"\n";
        for (String[] row:rows) {
            for(String s: row){
                res+=s;
            }
        }
        return res;
    }
}
