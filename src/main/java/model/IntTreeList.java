package model;

import java.util.ArrayList;
import java.util.List;

public class IntTreeList implements TreeList{
    private List<IntValue> values=new ArrayList<>();
    private List<RowValue> nullValue=new ArrayList<>();

    @Override
    public IntValue put(String token, RowValue row) {
        if(token.equals("")){
            putnull(row);
            return null;
        }
        int debut=0;
        int fin= values.size();
        int v1=(int)Integer.parseInt(token);
        IntValue value;
        if(fin==0){
            value=new IntValue(v1,row);
            values.add(value);
            return value;
        }
        int i=(fin-debut)/2;
        value=values.get(i);
        int v2=value.getValue();
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
            value=new IntValue(v1,row);
            values.add(i,value);
        }

        return value;
    }

    @Override
    public void putnull(RowValue row) {
        nullValue.add(row);
    }

    @Override
    public Value putfirst(String token, RowValue row) {
        if(token.equals("")){
            putnull(row);
            return null;
        }
        IntValue value=new IntValue((int)Integer.parseInt(token),row);
        values.add(value);
        return value;
    }
}
