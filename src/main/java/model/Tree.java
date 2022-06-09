package model;

import java.util.*;

public interface Tree {
    Object put(String token, RowTree row);

    List<RowTree> getEqual(String value);

    HashMap<String,List<RowTree>> getGroupBy();

    Set<RowTree> gt(String value);

    Set<RowTree> lt(String value);

    Set<RowTree> gte(String value);

    Set<RowTree> lte(String value);



   /* public Object put(String key, RowTree row);
    private void addEntryToEmptyMap(Object key, RowTree value) ;

    private void addEntry(Object key, RowTree value, Node p, boolean addToLeft);

    private void fixAfterInsertion(Node x) ;*/
}
