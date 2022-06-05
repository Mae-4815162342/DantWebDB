package model;

import java.util.List;

public class RowTree {
    private String[] values;
    public RowTree(String[] values){
        this.values=values;
    }

    public String selectAll(){
        String res="";
        for(String s: values){
            res+=s;
        }
        return res+"\n";
    }

}
