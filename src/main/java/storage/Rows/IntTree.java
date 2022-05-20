package storage.Rows;

import java.util.ArrayList;
import java.util.TreeMap;

public class IntTree implements Tree {
    private ArrayList<Integer> nullRows = new ArrayList<>();
    private TreeMap<Integer, ArrayList<Integer>> tree = new TreeMap<Integer, ArrayList<Integer>>();

    public IntTree(){}

    public void insertnull(int n){
        nullRows.add(n);
        return;
    }

    public void insertValue(int n,int value){
        ArrayList<Integer> rows = tree.get(value);
        if (rows == null) {
            rows = new ArrayList<>();
            rows.add(n);
            tree.put(value, rows);
            return;
        }
        rows.add(n);
        return;
    }

    public ArrayList<Integer> selectnull(){
        return nullRows;
    }

    public ArrayList<Integer> selectequals(int v){
        return tree.get(v);
    }





}