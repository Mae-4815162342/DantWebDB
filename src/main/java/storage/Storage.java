package storage;

import java.util.ArrayList;
import java.util.List;

/* interface générique pour stocker les données */

public interface Storage<T>{

    void insert(T... line);

    ArrayList<String> selectEquals(int columnIndex, String value);

    void insert(List<T> lines);

    List<T> selectAll();

}
