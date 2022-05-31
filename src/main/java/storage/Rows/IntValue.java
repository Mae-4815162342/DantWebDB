package storage.Rows;

import java.util.ArrayList;

public class IntValue implements Type<Integer> {
    private int value;
    private IntValue fils1;
    private IntValue fils2;
    private ArrayList<RowObject> rows=new ArrayList<>();

    public IntValue(String v){
        this.value=Integer.parseInt(v);
        this.fils1=null;
        this.fils2=null;
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
        return (v==value && b) ;
    }

    @Override
    public boolean notequal(Integer v, boolean b) {
        return (v!=value && b) ;
    }

    @Override
    public boolean gte(Integer v, boolean b) {
        return (value>=v && b);
    }

    @Override
    public boolean gt(Integer v, boolean b) {
        return (value>v && b);
    }

    @Override
    public boolean lte(Integer v, boolean b) {
        return (value<=v && b);
    }

    @Override
    public boolean lt(Integer v, boolean b) {
        return (value<v && b);
    }

}
