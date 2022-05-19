package storage;

import model.Row;

import java.util.ArrayList;
import java.util.List;

public class RowStorage implements Storage<Row> {

    private final List<Row> rows = new ArrayList<>();

    @Override
    public synchronized void insert(Row row) {
        rows.add(row);
    }

    @Override
    public void insert(List<Row> rows) {
        this.rows.addAll(rows);
    }

    @Override
    public List<Row> selectAll() {
        return rows;
    }
}
