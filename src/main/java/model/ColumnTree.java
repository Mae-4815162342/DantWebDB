package model;

import java.util.*;

public class ColumnTree {
    private String type;
    private IndexList nulllist;
    private TreeMap<String,IndexList> valuelist;



    public ColumnTree(String type){
        this.type=type;
        this.valuelist=new TreeMap<>();

    }

    public void insert(String e,int row){
        if(e.isEmpty()){
            IndexList.addElement(row,nulllist);
            return;
        }
        IndexList l=valuelist.get(e);
        l=IndexList.addElement(row,l);
        valuelist.put(e,l);
    }



}



