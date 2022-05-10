package storage;

public interface Column<T> {

    public T equal(T value);
    public T notequals(T value);
    public T gt(T value);
    public T gte(T value);
    public T lt(T value);
    public T lte(T value);

}
