package storage.Rows;

import storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RowObjectStorage implements Storage<RowObject> {
    private List<RowObject> rows=new ArrayList<>();

    @Override
    public void insert(RowObject... line) {
        rows.addAll(Arrays.asList(line));
    }

    @Override
    public void insert(List<RowObject> lines) {
        this.rows.addAll(lines);
    }

    @Override
    public List<RowObject> selectAll() {
        return rows;
    }

    @Override
    public void delete(RowObject line) {

    }
}
