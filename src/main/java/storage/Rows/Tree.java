package storage.Rows;

import java.util.ArrayList;

public interface Tree {

    public void insertnull(int n);
    public void insertValue(int n,int value);

    public ArrayList<Integer> selectnull();

    public ArrayList<Integer> selectequals(int v);
}
