package model;

import java.util.ArrayList;
import java.util.List;

public class RowValue {

    private List<Value> values;
    public RowValue(List<Value> values){
        this.values=values;
    }
    public RowValue(){
        values=new ArrayList<>();
    }
    public void insert(Value value){
        values.add(value);
    }
    public String selectAll(){
        String res="";
        for(Value s: values){
            res+=s.toString();
        }
        return res+"\n";
    }
}
