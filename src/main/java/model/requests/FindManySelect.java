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

public class FindManySelect implements SelectSchema {
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, Filter> where;
  private HashMap<String, String> orderBy;
  private String groupBy;
  private List<HashMap<String,String>> result;
  private Table table;
  private List<String> columnLabel;
  private LinkedHashMap<String, String> columns;
  private boolean fromClient;
  
  private synchronized void handleRow(Row row, HashMap<String, String> rowFromMachine){
    if(row != null) {
      List<String> values = row.toList();
      boolean valid = true;
      if (where != null) {
        for (String targetedColumn : where.keySet()) {
          if (valid) {
            Filter UserFilter = where.get(targetedColumn);
            String value = values.get(columnLabel.indexOf(targetedColumn));
            valid = (!value.equals("") ? valid && UserFilter.evaluate(value, columns.get(targetedColumn)) : false);
          }
        }
      }

      if (valid) {
        HashMap<String, String> resulatRow = new HashMap<String, String>();
        for (String targetedColumn : select) {
          resulatRow.put(targetedColumn, values.get(columnLabel.indexOf(targetedColumn)));
        }
        result.add(resulatRow);
        if (limit != -1) {
          limit = limit - 1;
        }
      }
    } else {
      result.add(rowFromMachine);
      if (limit != -1) {
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

  public void run() throws ColumnNotExistsException, InvalidSelectRequestException {
    List<Row> lines = table.getLines().selectAll();
    for (Row row : lines) {
      handleRow(row, null);
      if (limit == 0) {
        break;
      }
    }
  }

  @Override
  public void setRequest(Table table, boolean fromClient) throws Exception{
    this.table = table;
    this.fromClient = fromClient;
    if (select == null) {
      select = table.getColumns().keySet();
    }
    columnLabel = new ArrayList<String>(table.getColumns().keySet());
    result = new ArrayList<>();
    columns = table.getColumns();
  }

  public int getLimit() {
    return this.limit;
  }

  @Override
  public boolean hasWhere() {
    return this.where != null;
  }

  @Override
  public boolean hasSelect() {
    return this.select != null;
  }

  @Override
  public void addLines(List<HashMap<String, String>> lines) {
    if(limit == 0){
      return;
    }
    for(HashMap<String, String> row : (ArrayList<HashMap<String, String>>) lines){
      handleRow(null, row);
      if(limit == 0){
        break;
      }
    }
  }

  @Override
  public Object getRes() {
    if(orderBy!=null && fromClient){
      orderResult(result, table.getColumns());
    }
    return result;
  }
}
