package storage;

import database.Row;

import java.util.List;

public interface Storage {

    void insert(Row... row);

    void insert(List<Row> rows);

    List<Row> selectAll();

}
