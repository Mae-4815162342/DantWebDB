package model;

import storage.RowStorage;

import java.util.*;
import java.util.stream.IntStream;

public class TableTree {
    private String tableName;
    private ArrayList<RowTree> rows;
    private LinkedHashMap<String, String> columns;
    private int len=0;
    private ArrayList<Tree> values;


    public TableTree(String name, LinkedHashMap<String, String> columnsEntry){
        this.tableName=name;
        this.columns=columnsEntry;
        this.rows=new ArrayList<>();
        this.values=new ArrayList<>();
        for(Map.Entry<String,String> c:columnsEntry.entrySet()){
            values.add(new Tree());
        }
        System.out.println(this.tableName+" created");
    }



    public String getName() {
        return this.tableName;
    }

    public  void insertEntry(String line) {
        String[]tokens = line.split(";|,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)*(?=(?:[^;]*\"[^;]*\")*[^;]*$)");
        RowTree row= new RowTree();

        int len= rows.size();
        int clen=columns.size()-1;
        /*IntStream.range(0, clen).parallel().forEach(i -> {
            row.add(values.get(i).insert(tokens[i],len));
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/

        //System.out.println(len);
        for(int i=0;i<columns.size();i++){
            //row[i]=
            row.insert(values.get(i).put(tokens[i],row));
            //System.out.println(len+" "+tokens[i]+" inserted ");

        }
        rows.add(row);
        //if(len%100000==0)
        //System.out.println(len);

    }

    public int getTableSize() {
        return rows.size();
    }
    public List<RowTree> getLines() {
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
            values.add(new Tree());
        }
        System.out.println(this.tableName+" created");
    }

    public String getall() {
        String res="Number\tof\trow :" +rows.size()+"\n";
        for (RowTree row:rows) {
            //res+=row.selectAll();
            System.out.println(row.selectAll());
        }
        return res;
    }
}
