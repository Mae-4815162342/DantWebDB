package model.requests;

import java.util.ArrayList;
import java.util.Collection;
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

public class GroupBy implements BasicSchema{
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, Filter> where;
  private HashMap<String, String> orderBy;
  private Set<String> by;
  private HashMap<String, HashMap<String, Integer>> having;
  private Set<String> _count;
  private Set<String> _avg;
  private Set<String> _max;
  private Set<String> _sum;
  private Set<String> _min;

  private Set<String> createKey(Row row, List<String> columnLabel) throws ColumnNotExistsException{
    Set<String> res = new HashSet<>();
    List<String> rowList = row.toList();
    for(String groupElement : by){
      res.add(rowList.get(columnLabel.indexOf(groupElement)));
    }
    return res;
  } 

  private HashMap<String, Object> createGroup(Row row, List<String> columnLabel, LinkedHashMap<String, String> columns) throws ColumnNotExistsException{
    HashMap<String, Object> res = new HashMap<>();
    List<String> rowList = row.toList();
    for(String groupElement : by){
      res.put(groupElement, rowList.get(columnLabel.indexOf(groupElement)));
    }
    try{
      checkAggregate(columns, res);
    }
    catch(ColumnNotExistsException e){
      throw e;
    }
    return res;
  }  
  private void calculateAverage(HashMap<Set<String>, Integer> groupWorkforce, HashMap<Set<String>, HashMap<String, Object>> res){
    if(_avg!=null){
      for(Set<String> groupName : res.keySet()){
        HashMap<String,Double> oldValues = (HashMap<String,Double>) res.get(groupName).get("_avg");
        for(String targetedlabels : oldValues.keySet()){
          double oldValue = oldValues.get(targetedlabels);
          oldValues.replace(targetedlabels, oldValue, Math.round(oldValue/groupWorkforce.get(groupName)*100.0)/100.0);
        }
      }
      
    }
  }
  private void setupKeyValue(HashMap<String, Object> group,  Set<String> operation, String operationLabel){
    HashMap<String,Double> values = new HashMap<>();
    for(String targetedLabels : operation){
      values.put(targetedLabels, 0.0);
    }
    group.put(operationLabel, values);
  }

  private void checkAggregate(LinkedHashMap<String, String> columns, HashMap<String, Object> group) throws ColumnNotExistsException{
    if(_count != null){
      Set<String> copy = new HashSet<>(_count) ;
      copy.removeIf(filter -> !((columns.containsKey(filter) && columns.get(filter).equals("int"))) || !filter.equals("_all"));      
      if(copy.size()==0){
        setupKeyValue(group, _count, "_count");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_avg != null){
      Set<String> copy = new HashSet<>(_avg) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(group, _avg, "_avg");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_sum != null){
      Set<String> copy = new HashSet<>(_sum) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(group, _sum, "_sum");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_min != null){
      Set<String> copy = new HashSet<>(_min) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(group, _min, "_min");
      } else {
        throw new ColumnNotExistsException();
      }
    }
    if(_max != null){
      Set<String> copy = new HashSet<>(_max) ;
      copy.removeIf(filter -> columns.containsKey(filter) && columns.get(filter).equals("int"));      
      if(copy.size()==0){
        setupKeyValue(group, _max, "_max");
      } else {
        throw new ColumnNotExistsException();
      }
    }
  } 

  private void count(Row row, List<String> columnLabel,  HashMap<String, Object> group){
    if(_count!=null){
      HashMap<String,Double> value = (HashMap<String,Double>) group.get("_count");
      List<String> rowList = row.toList();
      for(String targetLabels : _count){
        if(targetLabels.equals("_all")|| (!targetLabels.equals("_all") && !rowList.get(columnLabel.indexOf(targetLabels)).equals(""))){
          value.replace(targetLabels, value.get(targetLabels), value.get(targetLabels) + 1);
        } 
      }
    }
  }
  private void average(Row row, List<String> columnLabel,  HashMap<String, Object> group){
    if(_avg!=null){

      HashMap<String,Double> value = (HashMap<String,Double>) group.get("_avg");
      List<String> rowList = row.toList();
      for(String targetLabels : _avg){
        if(!rowList.get(columnLabel.indexOf(targetLabels)).equals("")){
          double oldValue = (double) value.get(targetLabels);
          double newValue = Double.parseDouble(rowList.get(columnLabel.indexOf(targetLabels)));
          value.replace(targetLabels, oldValue , oldValue + newValue);
        }
      }
    }
  }
  private void maximum(Row row, List<String> columnLabel, HashMap<String, Object> group){
    if(_max!=null){
      HashMap<String,Double> value = (HashMap<String,Double>) group.get("_max");
      List<String> rowList = row.toList();
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
  private void minimum(Row row, List<String> columnLabel,  HashMap<String, Object> group){
    if(_min!=null){
      HashMap<String,Double> value = (HashMap<String,Double>) group.get("_min");
      List<String> rowList = row.toList();
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
  private void sum(Row row, List<String> columnLabel,  HashMap<String, Object> group){
    if(_sum!=null){
      HashMap<String,Double> value =  (HashMap<String,Double>) group.get("_sum");
      List<String> rowList = row.toList();
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

  private void handleRow(HashMap<Set<String>, Integer> groupWorkforce, HashMap<Set<String>, HashMap<String, Object>> groups, Row row,  List<String> columnLabel,  List<HashMap<String, Object>> res, LinkedHashMap<String, String> columns) throws ColumnNotExistsException{
    List<String> values = row.toList();
    boolean valid = true;
    if(where!=null){
      for(String targetColumn : where.keySet()){
        if(valid){
          Filter UserFilter = where.get(targetColumn);
          String value = values.get(columnLabel.indexOf(targetColumn));
					valid = (!value.equals("") ? valid && UserFilter.evaluate(value, columns.get(targetColumn)) : false);
        }
      }
    }
    if(valid){
      Set<String> groupName = createKey(row, columnLabel);
      if(!groups.keySet().contains(groupName)){
        groups.put(groupName, createGroup(row, columnLabel, columns));
        groupWorkforce.put(groupName, 1);
      }
      HashMap<String, Object> group = groups.get(groupName);
      count(row, columnLabel, group);
      average(row, columnLabel, group);
      maximum(row, columnLabel, group);
      minimum(row, columnLabel, group);
      sum(row, columnLabel, group);
      groupWorkforce.put(groupName,groupWorkforce.get(groupName) + 1);
      if(limit != -1){
        limit = limit - 1;
      }
    }
  }

  public Collection<HashMap<String, Object>> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
    if(select==null){
      select = table.getColumns().keySet(); 
    }
    HashMap<Set<String>, Integer> groupWorkforce = new HashMap<>();
    HashMap<Set<String>, HashMap<String, Object>> groups = new HashMap<>();
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<HashMap<String, Object>> res = new ArrayList<>();
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      handleRow(groupWorkforce, groups, row, columnLabel, res, table.getColumns());
      if(limit == 0){
        break;
      }
    }
    calculateAverage(groupWorkforce, groups);
    return groups.values();
  }
}
