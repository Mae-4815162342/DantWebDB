package model;

public interface TreeList {

    Value put(String token, RowValue row);

    void putnull(RowValue row);

    Value putfirst(String token, RowValue row);
}
