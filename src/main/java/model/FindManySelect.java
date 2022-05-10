package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.WrongGroupBy;

public class FindManySelect implements SelectInterface{
  private HashMap<String, Boolean> select;
  private HashMap<String, String> where;
  private String groupBy;
  private Integer limit;

  public List<?> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException, WrongGroupBy {
    Set<String> selectedLabels;
    Boolean hasGroupBy = groupBy != null;
    if(select==null){
      selectedLabels = table.getColumns().keySet(); 
    }
    else{
      selectedLabels = select.keySet();
    }
    if(hasGroupBy && !selectedLabels.contains(groupBy))
      throw new WrongGroupBy("GroupBy argument is not a column of selected table");
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<HashMap<String,String>> res = null;
    HashMap<String, List<HashMap<String, String>>> resGroupBy = null;
    if(hasGroupBy) {
      resGroupBy = new HashMap<>();
    } else {
      res = new ArrayList<>();
    }
    List<Row> lines = table.getLines().selectAll();
    int i = 0;
    for(Row row : lines){
      if(i++ >= limit) break;
      ArrayList<String> values = row.getColumnValuesMap();
      HashMap<String, String> resulatRow = new HashMap<String, String>();
      boolean valid = true;
      for(String targetColumn : selectedLabels){
        int index = columnLabel.indexOf(targetColumn);
        if(index >= 0) {
          if(where.get(targetColumn) == null) {
            //en cas de group by il ne faut pas ajouter la valeur dans le row
            if(!targetColumn.equals(groupBy)) resulatRow.put(targetColumn, values.get(index));
          } else {
            if (!where.get(targetColumn).equals(values.get(index))) {
              valid = false;
            } else {
              //en cas de group by il ne faut pas ajouter la valeur dans le row
              if(!targetColumn.equals(groupBy)) resulatRow.put(targetColumn, values.get(index));
            }
          }
        }
      }
      if(valid){
        if(hasGroupBy) {
          String columnValue = values.get(columnLabel.indexOf(groupBy));
          List<HashMap<String,String>> temp = resGroupBy.get(columnValue);
          if(temp == null) {
            resGroupBy.put(columnValue, new ArrayList<>());
            temp = resGroupBy.get(columnValue);
          }
          temp.add(resulatRow);
        } else {
          res.add(resulatRow);
        }
      }
    }
    List<HashMap<String, List<HashMap<String, String>>>> resGB = new ArrayList<>();
    resGB.add(resGroupBy);
    return hasGroupBy ? resGB : res;
  }
}
