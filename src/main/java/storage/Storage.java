package storage;

import model.Row;

import java.util.List;

/* interface générique pour stocker les données */

public interface Storage {

    void insert(Row... line);

    void insert(List<Row> lines);

    List<Row> selectAll();

}
