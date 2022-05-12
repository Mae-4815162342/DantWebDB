package model.requests.filters_operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import model.Row;

public class OR implements Operator{
  private HashMap<String, Filter> OR;

  public boolean evaluate(Row row, LinkedHashMap<String, String> columns){
    List<String> columnLabel = new ArrayList<>(columns.keySet());
    List<String> values = row.toList();
    boolean res = true;
    for(String targetColumn : OR.keySet()){
          Filter queries = (Filter) OR.get(targetColumn);
          String value = values.get(columnLabel.indexOf(targetColumn));
          res = (!queries.evaluate(value, columns.get(targetColumn)) || res);
    }
    return res;
  }
}
