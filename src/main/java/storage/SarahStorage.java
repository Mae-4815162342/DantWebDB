package storage;

import database.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SarahStorage implements Storage {

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
}
