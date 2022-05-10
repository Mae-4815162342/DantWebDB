package storage.Rows;

import storage.Rows.Type;

import java.util.Date;

public class DateValue implements Type<Date> {
    private Date value;

    @Override
    public Date getValue() {
        return this.value;
    }

    @Override
    public Date setValue(Date v) {
        this.value=v;
        return this.value;
    }

    @Override
    public boolean equal(Date v, boolean b) {
        return (v.equals(value) && b) ;
    }

    @Override
    public boolean notequal(Date v, boolean b) {
        return (!(v.equals(value)) && b) ;
    }

    @Override
    public boolean gte(Date v, boolean b) {
        return (value.compareTo(v)>=0 && b);
    }

    @Override
    public boolean gt(Date v, boolean b) {
        return (value.compareTo(v)>0 && b);
    }

    @Override
    public boolean lte(Date v, boolean b) {
        return (value.compareTo(v)<=0 && b);
    }

    @Override
    public boolean lt(Date v, boolean b) {
        return (value.compareTo(v)<0 && b);
    }
}
