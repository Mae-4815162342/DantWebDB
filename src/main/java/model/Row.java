package model;


public class Row {
    /* array list containing every column of a row */
    private String columnValuesMap;

    public Row(String line) {
        this.columnValuesMap = line;
    }

    public String getColumnValuesMap() {
        return this.columnValuesMap;
    }

    public void addRow(String columnValuesMap) {
        this.columnValuesMap = columnValuesMap;
    }
}
