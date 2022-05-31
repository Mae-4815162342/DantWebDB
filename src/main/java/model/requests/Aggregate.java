package model.requests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import model.Row;
import model.Table;
import model.requests.filters_operators.Filter;

public class Aggregate implements SelectSchema{
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, Filter> where;
  private HashMap<String, String> orderBy;
  private Set<String> _count;
  private Set<String> _avg;
  private Set<String> _max;
  private Set<String> _sum;
  private Set<String> _min;
  private int average_total = 0;
  private Table table;
  private boolean fromClient;
  private List<String> columnLabel;
  private LinkedHashMap<String, String> columns;
  private HashMap<String,HashMap<String,Double>> result;

  private void calculateAverage(HashMap<String, HashMap<String,Double>> res){
    if(_avg!=null){
      HashMap<String,Double> oldValues = res.get("_avg");
      for(String targetedlabels : oldValues.keySet()){
        double oldValue = oldValues.get(targetedlabels);
        oldValues.replace(targetedlabels, oldValue, Math.round(oldValue/average_total*100.0)/100.0);
      }
    }
  }
  private void setupKeyValue(HashMap<String, HashMap<String,Double>> res,  Set<String> operation, String operationLabel){
    HashMap<String,Double> values = new HashMap<>();
    for(String targetedLabels : operation){
      values.put(targetedLabels, 0.0);
    }
    res.put(operationLabel, values);
  }

  private void checkAggregate(HashMap<String, HashMap<String,Double>> res) throws ColumnNotExistsException{
    if(_count != null){
      Set<String> copy = new HashSet<>(_count) ;
      copy.removeIf(filter -> !((columns.containsKey(filter) && columns.get(filter).equals("int"))) || !filter.equals("_all"));      
      if(copy.size()==0){
        setupKeyValue(res, _count, "_count");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_avg != null){
      Set<String> copy = new HashSet<>(_avg) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(res, _avg, "_avg");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_sum != null){
      Set<String> copy = new HashSet<>(_sum) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(res, _sum, "_sum");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_min != null){
      Set<String> copy = new HashSet<>(_min) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(res, _min, "_min");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_max != null){
      Set<String> copy = new HashSet<>(_max) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(res, _max, "_max");
      } else {
        throw new ColumnNotExistsException();
      }
    }
  } 

  private void count(Row row, HashMap<String, String> machineRow){
    if(_count!=null){
      HashMap<String,Double> value = result.get("_count");
      List<String> rowList;
      if(row != null) {
        rowList = row.toList();
      } else {
        rowList = (List<String>) machineRow.values();
      }
      for(String targetLabels : _count){
        if(targetLabels.equals("_all")|| (!targetLabels.equals("_all") && !rowList.get(columnLabel.indexOf(targetLabels)).equals(""))){
          value.replace(targetLabels, value.get(targetLabels), value.get(targetLabels) + 1);
        } 
      }
    }
  }
  private void average(Row row, HashMap<String, String> machineRow){
    if(_avg!=null){
      average_total++;
      HashMap<String,Double> value = result.get("_avg");
      List<String> rowList;
      if(row != null) {
        rowList = row.toList();
      } else {
        rowList = (List<String>) machineRow.values();
      }
      for(String targetLabels : _avg){
        if(!rowList.get(columnLabel.indexOf(targetLabels)).equals("")){
          double oldValue = (double) value.get(targetLabels);
          double newValue = Double.parseDouble(rowList.get(columnLabel.indexOf(targetLabels)));
          value.replace(targetLabels, oldValue , oldValue + newValue);
        }
      }
    }
  }
  private void maximum(Row row, HashMap<String, String> machineRow){
    if(_max!=null){
      HashMap<String,Double> value = result.get("_max");
      List<String> rowList;
      if(row != null) {
        rowList = row.toList();
      } else {
        rowList = (List<String>) machineRow.values();
      }
      for(String targetLabels : _max){
        if(!rowList.get(columnLabel.indexOf(targetLabels)).equals("")){
          double oldValue = (double) value.get(targetLabels);
          double newValue = Double.parseDouble(rowList.get(columnLabel.indexOf(targetLabels)));
          if(newValue > oldValue){
            value.replace(targetLabels, oldValue, newValue);
          }
        }
      }
    }
  }

  private void minimum(Row row, HashMap<String, String> machineRow){
    if(_min!=null){
      HashMap<String,Double> value = result.get("_min");
      List<String> rowList;
      if(row != null) {
        rowList = row.toList();
      } else {
        rowList = (List<String>) machineRow.values();
      }
      for(String targetLabels : _min){
        if(!rowList.get(columnLabel.indexOf(targetLabels)).equals("")){
          double oldValue = (double) value.get(targetLabels);
          double newValue = Double.parseDouble(rowList.get(columnLabel.indexOf(targetLabels)));
          if(newValue < oldValue){
            value.replace(targetLabels, oldValue, newValue);
          }
        }
      }
    }
  }
  private void sum(Row row, HashMap<String, String> machineRow){
    if(_sum!=null){
      HashMap<String,Double> value = result.get("_sum");
      List<String> rowList;
      if(row != null) {
        rowList = row.toList();
      } else {
        rowList = (List<String>) machineRow.values();
      }
      for(String targetLabels : _sum){
        if(!rowList.get(columnLabel.indexOf(targetLabels)).equals("")){
          double oldValue = (double) value.get(targetLabels);
          double newValue = Double.parseDouble(rowList.get(columnLabel.indexOf(targetLabels)));
          value.replace(targetLabels, oldValue, oldValue + newValue);
        }
      }
    }
  }
  /* private List<HashMap<String,String>> orderResult(List<HashMap<String,String>> res, LinkedHashMap<String, String> columnLabel){
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
  } */

  private synchronized void handleRow(Row row, HashMap<String, String> machineRow){
    boolean valid = true;
    if(row != null) {
      List<String> values = row.toList();
      if (where != null) {
        for (String targetColumn : where.keySet()) {
          if (valid) {
            Filter UserFilter = where.get(targetColumn);
            String value = values.get(columnLabel.indexOf(targetColumn));
            valid = (!value.equals("") ? valid && UserFilter.evaluate(value, columns.get(targetColumn)) : false);
          }
        }
      }
    }
    if(valid){
      count(row, machineRow);
      average(row, machineRow);
      maximum(row, machineRow);
      minimum(row, machineRow);
      sum(row, machineRow);
      if(limit != -1){
        limit = limit - 1;
      }
    }
  }

  public void run() throws ColumnNotExistsException, InvalidSelectRequestException {
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      handleRow(row, null);
      if(limit == 0){
        break;
      }
    }
  }

  @Override
  public Object getRes() {
    calculateAverage(result);
    return result;
  }

  @Override
  public void setRequest(Table table, boolean fromClient) throws Exception {
    this.table = table;
    this.fromClient = fromClient;
    if(select==null){
      select = table.getColumns().keySet();
    }
    columnLabel = new ArrayList<String>(table.getColumns().keySet());
    columns = table.getColumns();
    result = new HashMap<>();
    checkAggregate(result);
  }

  @Override
  public int getLimit() {
    return -1;
  }

  @Override
  public boolean hasSelect() {
    return select != null;
  }

  @Override
  public boolean hasWhere() {
    return where != null;
  }

  @Override
  public void addLines(Object lines) {
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
}
