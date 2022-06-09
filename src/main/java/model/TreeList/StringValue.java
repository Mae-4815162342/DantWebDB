package model.TreeList;

import java.util.ArrayList;
import java.util.List;

public class StringValue implements Value {
    private String value;
    private List<RowValue> rows;

    public StringValue(String value,RowValue row){
        this.value=value;
        this.rows=new ArrayList<>();
        this.rows.add(row);
    }

    public String getValue() {
        return value;
    }

    public List<RowValue> getRows() {
        return rows;
    }

    public String toString(){
        return value;
    }


    public void add(RowValue value){
        this.rows.add(value);
    }
}
