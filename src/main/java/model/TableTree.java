package model;

import storage.RowStorage;

import java.util.*;
import java.util.stream.IntStream;

public class TableTree {
    private String tableName;
    private ArrayList<RowTree> rows;
    private LinkedHashMap<String, String> columns;
    private List<String> columnName;
    private int len=0;
    private ArrayList<Tree> values;
    //private ArrayList<TreeList> values;


    public TableTree(String name, LinkedHashMap<String, String> columnsEntry){
        this.tableName=name;
        this.columns=columnsEntry;
        this.rows=new ArrayList<>();
        this.values=new ArrayList<>();
        this.columnName=new ArrayList<>();
        for(Map.Entry<String,String> c:columnsEntry.entrySet()){
            values.add(new StringTree());
            columnName.add(c.getKey());
        }

       // List<String> keys = new ArrayList<>(this.columns.keySet());

        //initializeColumn();
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
        //RowValue row= new RowValue();
        RowTree row= new RowTree();

        //int len= rows.size();
        //int clen=columns.size()-1;
        /*IntStream.range(0, clen).parallel().forEach(i -> {
            row.add(values.get(i).insert(tokens[i],len));
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/

       // System.out.println(tokens.length);
        //System.out.println(columns.size());
       /* IntStream.range(0,tokens.length-1 ).parallel().forEach(i -> {
           // System.out.println("index "+i);
            row.insert((String) values.get(i).put(null,row));
        });


        IntStream.range(tokens.length,columns.size()-1 ).parallel().forEach(i -> {
            row.insert((String) values.get(i).put(tokens[i],row));
            //System.out.println(len+" "+tokens[i]+" inserted ");
        });*/
        for(int i= 0;i<tokens.length;i++){
            //row[i]=

            row.insert((String) values.get(i).put(tokens[i],row));
            //values.get(i).put(null,row);
            System.out.println(columnName.get(i)+" "+values.get(i)+" "+tokens[i]+" inserted ");

        }

        for(int i= tokens.length;i<columnName.size();i++){
            //row[i]=
            row.insert((String) values.get(i).put(null,row));
            //values.get(i).put(null,row);
            System.out.println(columnName.get(i)+" "+" "+tokens[i]+" inserted ");

        }

        len++;
        System.out.println(len);
        rows.add(row);
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
        this.columnName=new ArrayList<>();
       for(Map.Entry<String,String> c:this.columns.entrySet()){
            values.add(new StringTree());
           columnName.add(c.getKey());
        }
       //initializeColumn();

        //this.columnName=new ArrayList<>(this.columns.keySet());;
        //List<String> keys = new ArrayList<>(this.columns.keySet());

        System.out.println(this.tableName+" created");
    }

    /*public void initializeColumn(){
        for(Map.Entry<String,String> c:this.columns.entrySet()){
            System.out.println(c.getKey()+"   "+c.getValue());
            values.add(new StringTreeList());
          if(c.getValue().equals("float")){
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new FloatTreeList());}
            else if(c.getValue().equals("int")){
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new IntTreeList());}
            else{
               System.out.println(c.getKey()+"   "+c.getValue());
                values.add(new StringTreeList());}
        }
    }*/

    public List<HashMap<String,String>> getall() {
        System.out.println("here getall");

        List<HashMap<String,String>> res=new ArrayList<>();
        for (int i=0;i<rows.size();i++) {
            HashMap<String,String> h=new HashMap<>();
            List<String> row=rows.get(i).getValues();
            //System.out.println("row getall");
            for(int j=0;j< columnName.size();j++){
                //System.out.println("column getall");
                System.out.println(columns.get(j)+"      "+row.get(j));
                h.put(columnName.get(j),row.get(j));
            }
            res.add(h);
        }
        System.out.println("finish getall");
        return res;
       /* for (int i=0;i< values.size();i++){
            values.get(i).values();
        }*/

    }


    public List<RowTree> getEqual(String value,String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        List<RowTree> res=t.getEqual(value);
        return res;
    }

    public HashMap<String,List<RowTree>> groupby(String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        HashMap<String,List<RowTree>> res=t.getGroupBy();
        return res;
    }

    public Set<RowTree>gt(String value,String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        Set<RowTree> res=t.gt(value);
        return res;
    }

    public Set<RowTree>lt(String value,String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        Set<RowTree> res=t.lt(value);
        return res;
    }

    public Set<RowTree>gte(String value,String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        Set<RowTree> res=t.gte(value);
        return res;
    }

    public Set<RowTree>lte(String value,String column){
        int n=this.columnName.indexOf(column);
        Tree t=values.get(n);
        Set<RowTree> res=t.lte(value);
        return res;
    }

    public Set<RowTree>or(Set<RowTree> t1,Set<RowTree> t2){
        Set<RowTree> tres=new HashSet<>();
        tres.addAll(t1);
        tres.addAll(t2);
        return tres;
    }

    public Set<RowTree>and(Set<RowTree> t1,Set<RowTree> t2){
        Set<RowTree> tres=new HashSet<>();
        tres.addAll(t1);
        tres.retainAll(t2);
        return tres;
    }


    public Set<RowTree>not(Set<RowTree> t1){
        Set<RowTree> tres=new HashSet<>();
        tres.addAll(rows);
        tres.removeAll(t1);
        return tres;
    }

    public int count(Set<RowTree> t1){
        return t1.size();
    }
}
