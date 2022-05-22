package model;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;


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
    public List<String> toList() {
        return Splitter.on(Pattern.compile(";|,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).splitToList(columnValuesMap);
    }
}
