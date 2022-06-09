package model.TreeList;

public interface TreeList {

    Value put_(String token, RowValue row);

    void putnull(RowValue row);

    Value putfirst(String token, RowValue row);

    void values();
}
