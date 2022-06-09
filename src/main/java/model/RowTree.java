package model;

import java.util.ArrayList;
import java.util.List;

public class RowTree {
    private List<String> values;
    public RowTree(List<String> values){
        this.values=values;
    }
    public RowTree(){
        values=new ArrayList<>();
    }
    public void insert(String value){
        values.add(value);
    }
    public String selectAll(){
        String res="";
        for(String s: values){
            res+=s;
        }
        return res+"\n";
    }

    public List<String> getValues() {
        return values;
    }
}
