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

    public String insert(String e,int row){
        if(e.isEmpty()){
            IndexList.addElement(row,nulllist);
            return e;
        }
        String s;
        Object[] keys=valuelist.keySet().toArray();
        IndexList l=null;
        for( int i=0;i< valuelist.size();i++){
            if(valuelist.get(keys[i]).equals(e)){
                s=(String)keys[i];
                l=IndexList.addElement(row,valuelist.get(keys[i]));
                valuelist.put(e,l);
                return s;
            }

        }
        //IndexList l=valuelist.get(e);
        /*if(l==null){
            s=e;
        }*/
        l=IndexList.addElement(row,l);
        valuelist.put(e,l);
        return e;

    }



}



