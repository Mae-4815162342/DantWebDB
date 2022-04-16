package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.google.common.base.Splitter;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;

public class FindUniqueSelect implements SelectInterface{
  private HashMap<String, Boolean> select;
  private HashMap<String, String> where;

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
      List<String> values = Splitter.on(',').splitToList(row.getColumnValuesMap());
      System.out.println(values.size());
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
    }
    return null;
  } 
}
