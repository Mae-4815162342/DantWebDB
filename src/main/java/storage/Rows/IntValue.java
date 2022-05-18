package storage.Rows;

import java.util.ArrayList;

public class IntValue implements Type<Integer> {
    private Integer value;
    private IntValue fils1;
    private IntValue fils2;
    private ArrayList<RowObject> rows=new ArrayList<>();

    public IntValue(String v){
        this.value=Integer.parseInt(v);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Integer setValue(Integer v) {
        this.value=v;
        return this.value;
    }

    @Override
    public boolean equal(Integer v, boolean b) {
        return (v.equals(value) && b) ;
    }

    @Override
    public boolean notequal(Integer v, boolean b) {
        return (!(v.equals(value)) && b) ;
    }

    @Override
    public boolean gte(Integer v, boolean b) {
        return (value.compareTo(v)>=0 && b);
    }

    @Override
    public boolean gt(Integer v, boolean b) {
        return (value.compareTo(v)>0 && b);
    }

    @Override
    public boolean lte(Integer v, boolean b) {
        return (value.compareTo(v)<=0 && b);
    }

    @Override
    public boolean lt(Integer v, boolean b) {
        return (value.compareTo(v)<0 && b);
    }
}
