package model.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import model.Row;
import model.Table;

public class FindUniqueSelect implements BasicSchema{
  private HashMap<String, Boolean> select;
  private HashMap<String, String> where;

  private HashMap<String,String> handleRow(Table table, Row row, Set<String> selectedLabels, List<String> columnLabel){
    HashMap<String,String> res = new HashMap<>();
    List<String> values = row.toList();
    boolean valid = true;
    for(String targetColumn : where.keySet()){
      if(!where.get(targetColumn).equals(values.get(columnLabel.indexOf(targetColumn)))){
        valid = false;
      }
    }
    if(valid){
      for(String targetColumn : selectedLabels){
        res.put(targetColumn, values.get(columnLabel.indexOf(targetColumn)));
      }
      return res;
    }
    return null;
  }
  
  public HashMap<String,String> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
    if(where==null){
        throw new InvalidSelectRequestException();
    }
    Set<String> selectedLabels;
    if(select==null){
      selectedLabels = table.getColumns().keySet(); 
    }
    else{
      selectedLabels = select.keySet();
    }
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    HashMap<String,String> res = new HashMap<>();
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      res = handleRow(table, row, selectedLabels, columnLabel);
      if(res!=null){
        return res;
      }
    }
    return null;
  } 
}
