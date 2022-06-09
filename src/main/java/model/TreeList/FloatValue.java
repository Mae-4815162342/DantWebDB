package model.TreeList;

import java.util.ArrayList;
import java.util.List;

public class FloatValue implements Value {
    private float value;
    private List<RowValue> rows;

    public float getValue() {
        return value;
    }

    public List<RowValue> getRows() {
        return rows;
    }

    public FloatValue(float value, RowValue row){
        this.value=value;
        this.rows=new ArrayList<>();
        this.rows.add(row);
    }

    public void add(RowValue value){
        this.rows.add(value);
    }

    public String toString(){
        return (String.valueOf(value));
    }
}
