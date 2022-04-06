package storage;

import database.Row;

import java.util.List;

/* interface générique pour stocker les données */

public interface Storage {

    void insert(T... line);

    void insert(List<T> lines);

    List<T> selectAll();

}
