package storage.Rows;

import storage.Rows.Type;

public class FloatValue implements Type<Float> {
    private Float value;

    @Override
    public Float getValue() {
        return this.value;
    }

    @Override
    public Float setValue(Float v) {
        this.value=v;
        return this.value;
    }

    @Override
    public boolean equal(Float v, boolean b) {
        return (v.equals(value) && b) ;
    }

    @Override
    public boolean notequal(Float v, boolean b) {
        return (!(v.equals(value)) && b) ;
    }

    @Override
    public boolean gte(Float v, boolean b) {
        return (value.compareTo(v)>=0 && b);
    }

    @Override
    public boolean gt(Float v, boolean b) {
        return (value.compareTo(v)>0 && b);
    }

    @Override
    public boolean lte(Float v, boolean b) {
        return (value.compareTo(v)<=0 && b);
    }

    @Override
    public boolean lt(Float v, boolean b) {
        return (value.compareTo(v)<0 && b);
    }
}
