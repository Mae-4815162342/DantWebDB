package model;
import exception.ColumnNotInTableException;
import storage.RowStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Table {
    private String tableName;
    public LinkedHashMap<String, String> columns;
    private final RowStorage lines;

    public Table(String tableName, LinkedHashMap<String, String> columns) {
        this.tableName = tableName;
        this.lines = new RowStorage();
        this.columns = columns;
    }

    public void insertEntry(ArrayList<String> columnsMap) {
        Row row =  new Row(columnsMap);
        System.out.println("Inserting : " + columnsMap);
        lines.insert(row);
        System.out.println("Successfully added a row");
    }

    public HashMap<String, String> getColumns() {
        return columns;
    }

    public int getColumnIndex(String column) throws ColumnNotInTableException {
        if(columns.get(column) == null) throw new ColumnNotInTableException(tableName, column);
        Set<String> labels = columns.keySet();
        List<String> listLabels = new ArrayList<String>(labels);
        return listLabels.indexOf(column);
    }

    public RowStorage getLines() {
        return lines;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<String> getRowsWithColumnEquals(int columnIndex, String value) {
        return lines.selectEquals(columnIndex, value);
    }
}