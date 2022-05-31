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

public class Aggregate implements BasicSchema{
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, String> where;
  private HashMap<String, String> orderBy;
  private Set<String> _count;
  private Set<String> _avg;
  private Set<String> _max;
  private Set<String> _sum;
  private Set<String> _min;
  private int average_total = 0;

  private void calculateAverage(HashMap<String, HashMap<String,Integer>> res){
    if(_avg!=null){
      HashMap<String,Integer> oldValues = res.get("_avg");
      for(String targetedlabels : oldValues.keySet()){
        int oldValue = oldValues.get(targetedlabels);
        oldValues.replace(targetedlabels, oldValue, oldValue/average_total);
      }
    }
  }
  private void setupKeyValue(HashMap<String, HashMap<String,Integer>> res,  Set<String> operation, String operationLabel){
    HashMap<String,Integer> values = new HashMap<>();
    for(String targetedLabels : operation){
      values.put(targetedLabels, 0);
    }
    res.put(operationLabel, values);
  }

  private boolean checkAggregate(LinkedHashMap<String, String> columnLabel, HashMap<String, HashMap<String,Integer>> res){
    int check = 0;  Set<String> copy;
    if(_count != null){
      copy = _count;
      copy.removeIf(filter -> (!columnLabel.get(filter).equals("int")) || filter=="_all");      
      check += copy.size();
      if(copy.size()==0){
        setupKeyValue(res, _count, "_count");
      }
    }
    if(_avg != null){
      copy = _avg;
      copy.removeIf(filter -> (!columnLabel.get(filter).equals("int")));
      check += copy.size();
      if(copy.size()==0){
        setupKeyValue(res, _avg, "_avg");
      }
    }
    if(_sum != null){
      copy = _sum;
      copy.removeIf(filter -> (!columnLabel.get(filter).equals("int")));
      check += copy.size();
      if(copy.size()==0){
        setupKeyValue(res, _sum, "_sum");
      }
    }
    if(_min != null){
      copy = _min;
      copy.removeIf(filter -> (!columnLabel.get(filter).equals("int")));
      check += copy.size();
      if(copy.size()==0){
        setupKeyValue(res, _min, "_min");
      }
    }
    if(_max != null){
      copy = _max;
      copy.removeIf(filter -> (!columnLabel.get(filter).equals("int")));
      check += copy.size();
      if(copy.size()==0){
        setupKeyValue(res, _max, "_max");
      }
    }
    return (check == 0);
  } 

  private void count(HashMap<String, HashMap<String,Integer>> res){
    if(_count!=null){
      HashMap<String,Integer> value = res.get("_count");
      value.replace("_all", value.get("_all"), value.get("_all") + 1);
    }
  }
  private void average(Row row, List<String> columnLabel, HashMap<String, HashMap<String,Integer>> res){
    if(_avg!=null){
      average_total++;
      HashMap<String,Integer> value = res.get("_avg");
      List<String> rowList = row.toList();
      for(String targetLabels : _avg){
        int oldValue = value.get(targetLabels);
        int newValue = Integer.parseInt(rowList.get(columnLabel.indexOf(targetLabels)));
        value.replace(targetLabels, oldValue , oldValue + newValue);
      }
    }
  }
  private void maximum(Row row, List<String> columnLabel, HashMap<String, HashMap<String,Integer>> res){
    if(_max!=null){
      HashMap<String,Integer> value = res.get("_max");
      List<String> rowList = row.toList();
      for(String targetLabels : _max){
        int oldValue = value.get(targetLabels);
        int newValue = Integer.parseInt(rowList.get(columnLabel.indexOf(targetLabels)));
        if(newValue > oldValue){
          value.replace(targetLabels, oldValue, newValue);
        }
      }
    }
  }
  private void minimum(Row row, List<String> columnLabel, HashMap<String, HashMap<String,Integer>> res){
    if(_min!=null){
      HashMap<String,Integer> value = res.get("_min");
      List<String> rowList = row.toList();
      for(String targetLabels : _min){
        int oldValue = value.get(targetLabels);
        int newValue = Integer.parseInt(rowList.get(columnLabel.indexOf(targetLabels)));
        if(newValue < oldValue){
          value.replace(targetLabels, oldValue, newValue);
        }
      }
    }
  }
  private void sum(Row row, List<String> columnLabel, HashMap<String, HashMap<String,Integer>> res){
    if(_sum!=null){
      HashMap<String,Integer> value = res.get("_sum");
      List<String> rowList = row.toList();
      for(String targetLabels : _sum){
        int oldValue = value.get(targetLabels);
        int newValue = Integer.parseInt(rowList.get(columnLabel.indexOf(targetLabels)));
        value.replace(targetLabels, oldValue, oldValue + newValue);
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

  private void handleRow(Row row,  List<String> columnLabel,  HashMap<String, HashMap<String, Integer>> res){
    List<String> values = row.toList();
    System.out.println(values.size());
    boolean valid = true;
    if(where!=null){
      for(String targetColumn : where.keySet()){
        if(!where.get(targetColumn).equals(values.get(columnLabel.indexOf(targetColumn)))){
          valid = false;
        }
      }
    }
    if(valid){
      count(res);
      average(row, columnLabel, res);
      maximum(row, columnLabel, res);
      minimum(row, columnLabel, res);
      sum(row, columnLabel, res);
      if(limit != -1){
        limit = limit - 1;
      }
    }
  }

  public HashMap<String,HashMap<String,Integer>> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
   /* if(select==null){
      select = table.getColumns().keySet(); 
    }
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    HashMap<String, HashMap<String,Integer>> res = new HashMap<>();
    checkAggregate(table.getColumns(), res);
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      handleRow(row, columnLabel, res);
      if(limit == 0){
        break;
      }
    }
    calculateAverage(res);
    return res;*/
    return null;
  }
}
