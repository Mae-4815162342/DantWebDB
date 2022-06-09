package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;


public class Row {
    /* array list containing every column of a row */
    private String columnValuesMap;

    public Row(String line) {
        this.columnValuesMap = line;
    }

    public Row(HashMap<String, String> foreignRow, List<String> localColumns) {
        List<String> values = new ArrayList<>(foreignRow.values());
        List<String> columns = new ArrayList<>(foreignRow.keySet());
        String line = "";
        for(int i = 0; i < localColumns.size(); i++) {
            if(i == 0) {
                line = line + values.get(columns.indexOf(localColumns.get(i)));
            } else {
                line = line + "," + values.get(columns.indexOf(localColumns.get(i)));
            }
        }
        this.columnValuesMap = line;
    }

    public String getColumnValuesMap() {
        return this.columnValuesMap;
    }

    public void addRow(String columnValuesMap) {
        this.columnValuesMap = columnValuesMap;
    }
    public List<String> toList() {
        return Splitter.on(Pattern.compile(";|,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)*(?=(?:[^;]*\"[^;]*\")*[^;]*$)")).splitToList(columnValuesMap);
    }
}
