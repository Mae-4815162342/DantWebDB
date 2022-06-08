package model;

import storage.RowStorage;

import java.util.*;
import java.util.stream.IntStream;

public class TableTree {
    private String tableName;
    private ArrayList<RowValue> rows;
    private LinkedHashMap<String, String> columns;
    private int len=0;
    //private ArrayList<Tree> values;
    private ArrayList<TreeList> values;


    public TableTree(String name, LinkedHashMap<String, String> columnsEntry){
        this.tableName=name;
        this.columns=columnsEntry;
        //this.rows=new ArrayList<>();
        this.values=new ArrayList<>();
        /*for(Map.Entry<String,String> c:columnsEntry.entrySet()){
            values.add(new Tree());
        }*/

        initializeColumn();
        System.out.println(this.tableName+" created");
    }



    public String getName() {
        return this.tableName;
    }

    public  void insertEntry(String line) {
        String[]tokens = line.split(";|,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)*(?=(?:[^;]*\"[^;]*\")*[^;]*$)");
       /* for(int i=0;i< tokens.length;i++){
            System.out.println(tokens[i]);
        }*/
        RowValue row= new RowValue();
        //RowTree row= new RowTree();

        //int len= rows.size();
        //int clen=columns.size()-1;
        /*IntStream.range(0, clen).parallel().forEach(i -> {
            row.add(values.get(i).insert(tokens[i],len));
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/

        //System.out.println(len);
        /*IntStream.range(0, tokens.length-1).mapToObj(i -> {
            //System.out.println("--------------------------"+i);
            row.insert(values.get(i).put(tokens[i],row));
            values.get(i).put(tokens[i],row);
            //System.out.println(len+" "+tokens[i]+" inserted ");
        })
                .parallel();*/
       /* IntStream.range(tokens.length,columns.size() ).parallel().forEach(i -> {
            row.insert(values.get(i).put(null,row));
            values.get(i).put(null,row);
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/

        /*System.out.println(values.size());
        System.out.println(tokens.length);
        System.out.println();*/
        for(int i=0;i< tokens.length;i++){
            //System.out.println(tokens[i]);
            //row[i]=
            //row.insert(values.get(i).put(tokens[i],row));
            values.get(i).put_(tokens[i],row);
            //System.out.println(len+" "+tokens[i]+" inserted ");

        }
        for(int i= tokens.length;i<columns.size();i++){
            //row[i]=
            //row.insert(values.get(i).put(null,row));
            values.get(i).putnull(row);
            //System.out.println(len+" "+tokens[i]+" inserted ");

        }

        //len++;
        System.out.println(rows.size());
        //rows.add(row);
        //if(len%100000==0)

    }

    public int getTableSize() {
        //return rows.size();
        return len;
    }
    /*public List<RowTree> getLines() {
        return rows;
    }*/
    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }
    public void deleteEntry(Row row){
    }

    public void initialize() {
        this.values=new ArrayList<>();
        this.rows=new ArrayList<>();
       /*for(Map.Entry<String,String> c:this.columns.entrySet()){
            values.add(new Tree());
        }*/
       initializeColumn();
        System.out.println(this.tableName+" created");
    }

    public void initializeColumn(){
        for(Map.Entry<String,String> c:this.columns.entrySet()){
            System.out.println(c.getKey()+"   "+c.getValue());
            values.add(new StringTreeList());
          /* if(c.getValue().equals("float")){
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new FloatTreeList());}
            else if(c.getValue().equals("int")){
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new IntTreeList());}
            else{
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new StringTreeList());}*/
        }
    }

    public String getall() {
       /* String res="Number\tof\trow :" +rows.size()+"\n";
        for (RowTree row:rows) {
            res+=row.selectAll();
            System.out.println(row.selectAll());
        }
        return res;*/
        for (int i=0;i< values.size();i++){
            values.get(i).values();
        }
        return null;
    }
}
