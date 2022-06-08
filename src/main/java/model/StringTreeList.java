package model;

import java.util.ArrayList;
import java.util.List;

public class StringTreeList implements TreeList{
    private List<StringValue> values=new ArrayList<>();
    private List<RowValue> nullValue=new ArrayList<>();

    @Override
    public StringValue put_(String t, RowValue row) {
        //String v1=Float.parseFloat(token);
        System.out.println("ICI c'est string");
        if(t.equals("")){
            putnull(row);
            return null;
        }

        String token=t.toUpperCase();
        StringValue value;
        int debut=0;
        int fin= values.size()-1;
        int i=0;
        while(debut<fin){
            i=debut+(fin-debut)/2;
            value=values.get(i);
            String v2=value.getValue();
            int compare= token.compareTo(v2);
            if(compare==0){
                value.add(row);
                return value;
            }
            if(compare>0){debut=i+1;}
            else{fin=i-1;}

        }
        ///System.out.println("index-"+i+" champs: "+token);
        value=new StringValue(token,row);
        values.add(i,value);
        return value;
    }

    @Override
    public void putnull(RowValue row) {
        //System.out.println("nulllllllllll");
        nullValue.add(row);
    }

    @Override
    public StringValue putfirst(String token, RowValue row) {
        //System.out.println("hellloooooo");
        if(token.equals("")){
            putnull(row);
            return null;
        }
        StringValue value=new StringValue(token,row);
        values.add(value);
        return value;
    }

    public void values(){
        String s="[";
        for(int i=0;i< values.size();i++){
            s+=values.get(i)+",";
        }
        System.out.println(s+']') ;
    }
}
