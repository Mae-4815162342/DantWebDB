package model;

import java.util.ArrayList;
import java.util.List;

public class StringTreeList implements TreeList{
    private List<StringValue> values=new ArrayList<>();
    private List<RowValue> nullValue=new ArrayList<>();

    @Override
    public StringValue put(String token, RowValue row) {
        //String v1=Float.parseFloat(token);
        if(token.equals("")){
            putnull(row);
            return null;
        }
        int debut=0;
        int fin= values.size();
        StringValue value;
        if(fin==0){
            value=new StringValue(token,row);
            values.add(value);
            return value;
        }

        int i=(fin-debut)/2;

        //System.out.println("taille :"+values.size()+ "index :"+i);
        value=values.get(i);
        String v2=value.getValue();
        while(v2.equals(token) && debut<=fin){
            int compare= token.compareTo(v2);
            if(compare<0){
                debut=i+1;
            }
            else{
                fin=i-1;

            }
            value=values.get(i);
            v2=value.getValue();
            i=(fin-debut)/2;
        }
        if(v2!=token){
            value=new StringValue(token,row);
            values.add(i,value);
        }

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
}
