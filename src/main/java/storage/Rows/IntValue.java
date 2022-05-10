package storage.Rows;

import storage.Rows.Type;

public class IntValue implements Type<Integer> {
    private Integer value;

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
