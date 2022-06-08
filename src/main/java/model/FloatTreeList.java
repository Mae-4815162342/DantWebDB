package model;

import java.util.ArrayList;
import java.util.List;

public class FloatTreeList implements TreeList{
    private List<FloatValue> values=new ArrayList<>();
    private List<RowValue> nullValue=new ArrayList<>();


    @Override
    public FloatValue put_(String token, RowValue row) {
        System.out.println("ICI c'est float");
        float v1=Float.parseFloat(token);
        if(token.equals("")){
            putnull(row);
            return null;
        }
        int debut=0;
        int fin= values.size();
        FloatValue value;
        if(fin==0){
            value=new FloatValue(v1,row);
            values.add(value);
            return value;
        }
        fin-=1;

        int i=(int)((fin+debut)/2);
        int j=i;

        //System.out.println("taille :"+values.size()+ "index :"+i);
        value=values.get(i);
        float v2=value.getValue();
        while(v1!=v2 && debut<=fin){

            if(v1>v2){
                debut=i+1;
            }
            else{
                fin=i-1;

            }
            j=i;
            i=((fin+debut)/2);
            value=values.get(i);
            v2=value.getValue();
        }
        if(v1==v2){value.add(row);}
        else{
            value=new FloatValue(v1,row);
            values.add(j,value);
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

    public void values(){
        String s="[";
        for(int i=0;i< values.size();i++){
            s+=values.get(i)+",";
        }
        System.out.println(s+']') ;
    }

}
