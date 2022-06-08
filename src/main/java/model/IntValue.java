package model;

import java.util.ArrayList;
import java.util.List;

public class IntValue implements Value{
    private int value;
    private List<RowValue> rows;

    public int getValue() {
        return value;
    }

    public List<RowValue> getRows() {
        return rows;
    }

    public IntValue(int value,RowValue row){
        this.value=value;
        this.rows=new ArrayList<>();

        this.rows.add(row);
    }

    public void add(RowValue value){
        this.rows.add(value);
    }

    public String toString(){
        return String.valueOf(value);
    }
}
