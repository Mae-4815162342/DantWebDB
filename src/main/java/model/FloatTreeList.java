package model;

import java.util.ArrayList;
import java.util.List;

public class FloatTreeList implements TreeList{
    private List<FloatValue> values=new ArrayList<>();
    private List<RowValue> nullValue=new ArrayList<>();


    @Override
    public FloatValue put(String token, RowValue row) {
        if(token.equals("")){
            putnull(row);
            return null;
        }
        int debut=0;
        int fin= values.size();
        float v1=Float.parseFloat(token);
        FloatValue value;
        if(fin==0){
            value=new FloatValue(v1,row);
            values.add(value);
            return value;
        }
        int i=(fin-debut)/2;
        value=values.get(i);
        float v2=value.getValue();
        while(v2!=v1 && debut<=fin){
            if(v1>v2){
                debut=i+1;
            }
            else{
                fin=i-1;

            }
            value=values.get(i);
            v2=value.getValue();
            i=(fin-debut)/2;
        }
        if(v2!=v1){
            value=new FloatValue(v1,row);
            values.add(i,value);
        }

        return value;
    }

    @Override
    public void putnull(RowValue row) {
        nullValue.add(row);
    }

    @Override
    public FloatValue putfirst(String token, RowValue row) {
        if(token.equals("")){
            putnull(row);
            return null;
        }
        FloatValue value=new FloatValue(Float.parseFloat(token),row);
        values.add(value);
        return value;
    }

}
