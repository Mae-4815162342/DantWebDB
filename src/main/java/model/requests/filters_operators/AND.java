package model.requests.filters_operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import model.Row;

public class AND implements Operator{
  private HashMap<String, Object> AND;

  public boolean evaluate(Row row, LinkedHashMap<String, String> columns){
    List<String> columnLabel = new ArrayList<>(columns.keySet());
    List<String> values = row.toList();
    boolean res = true;
    for(String targetColumn : AND.keySet()){
       if(AND.get(targetColumn).getClass() == String.class){
          String query = (String) AND.get(targetColumn);
          String value = values.get(columnLabel.indexOf(targetColumn));
          res = (query.equals(value) && res);
       }
        if(AND.get(targetColumn).getClass() == Integer.class){
          int query = (int) AND.get(targetColumn);
          int value = Integer.parseInt(values.get(columnLabel.indexOf(targetColumn)));
          res = ((query == value) && res);
        }
        if(AND.get(targetColumn).getClass() == Filter.class){
          Filter queries = (Filter) AND.get(targetColumn);
          String value = values.get(columnLabel.indexOf(targetColumn));
          res = (queries.evaluate(value, columns.get(targetColumn)) && res);
        }
    }
    return res;
  }
}
