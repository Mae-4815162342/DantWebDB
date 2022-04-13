package storage;

import model.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RowStorage implements Storage<Row> {

    private final List<Row> rows = new ArrayList<>();

    @Override
    public void insert(Row... row) {
        rows.addAll(Arrays.asList(row));
    }

    @Override
    public void insert(List<Row> rows) {
        this.rows.addAll(rows);
    }

    @Override
    public List<Row> selectAll() {
        return rows;
    }

    @Override
    public ArrayList<String> selectEquals(int columnIndex, String value) {
        ArrayList<String> res = new ArrayList<>();
        for(Row r : rows) {
            if(r.getValueInColumn(columnIndex).equals(value)) {
                res.add(r.toString());
            }
        }
        return res;
    }
}
