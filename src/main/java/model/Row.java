package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Row {
    /* array list containing every column of a row */
    private ArrayList<String> columnValuesMap;

    public Row(ArrayList<String> columnValuesMap) {
        this.columnValuesMap = columnValuesMap;
    }

    public ArrayList<String> getColumnValuesMap() {
        return this.columnValuesMap;
    }

    public void addRow(ArrayList<String> columnValuesMap) {
        this.columnValuesMap = columnValuesMap;
    }
}
