package model.requests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import model.Row;
import model.Table;
import model.requests.filters_operators.Filter;
import model.requests.filters_operators.Operator;

public class FindManySelect implements BasicSchema{
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, Filter> where;
  private HashMap<String, String> orderBy;
  
  private void handleRow(Row row,  List<String> columnLabel,  List<HashMap<String,String>> res, LinkedHashMap<String, String> columns){
    List<String> values = row.toList();
    boolean valid = true;
    if(where!=null){
      for(String targetedColumn : where.keySet()){
        if(valid){
          Filter UserFilter = where.get(targetedColumn);
          String value = values.get(columnLabel.indexOf(targetedColumn));
					valid = (!value.equals("") ? valid && UserFilter.evaluate(value, columns.get(targetedColumn)) : false);
        }
      }
    }
    if(valid){
      HashMap<String, String> resulatRow = new HashMap<String,String>();
      for(String targetedColumn : select){
        resulatRow.put(targetedColumn, values.get(columnLabel.indexOf(targetedColumn)));
      }
      res.add(resulatRow);
      if(limit != -1){
        limit = limit - 1;
      }
    }
  }
  
  private List<HashMap<String,String>> orderResult(List<HashMap<String,String>> res, LinkedHashMap<String, String> columnLabel){
    res.sort(new Comparator<HashMap<String,String>>() {
      @Override
      public int compare(HashMap<String,String> a, HashMap<String,String> b) {
        int res = 0;
          for(String targetcolumns : orderBy.keySet()){
            String direction = orderBy.get(targetcolumns);
            int dir = direction.equals("asc") ? 1 : -1;
            String type = columnLabel.get(targetcolumns); 
            if(type.equals("int")){
              res += dir*Integer.compare(Integer.parseInt(a.get(targetcolumns)), Integer.parseInt(b.get(targetcolumns)));
            }
            else{
              res += dir*a.get(targetcolumns).compareToIgnoreCase(b.get(targetcolumns));
            }
          }
          return res;
      }
    });
    return null;
  }

  public List<HashMap<String,String>> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
    if(select==null){
      select = table.getColumns().keySet(); 
    }
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<HashMap<String,String>> res = new ArrayList<>();
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      handleRow(row, columnLabel, res, table.getColumns());
      if(limit == 0){
        break;
      }
    }
    if(orderBy!=null){
      orderResult(res, table.getColumns());
    }
    return res;
  }
}
