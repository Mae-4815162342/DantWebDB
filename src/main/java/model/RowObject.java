package model;

import java.util.ArrayList;

public class RowObject {
    private ArrayList<Object> columnValuesMap;

    public RowObject(ArrayList<Object> values){
        this.columnValuesMap=values;
    }

    public Object getValue(int i){
        return this.columnValuesMap.get(i);
    }




}
