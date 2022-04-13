package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;

public class FindManySelect implements SelectInterface{
  private int take = -1;
  private HashMap<String, Boolean> select;
  private HashMap<String, String> where;

  public List<HashMap<String,String>> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
    Set<String> selectedLabels;
    if(select==null){
      selectedLabels = table.getColumns().keySet(); 
    }
    else{
      selectedLabels = select.keySet();
    }
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<HashMap<String,String>> res = new ArrayList<>();
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      ArrayList<String> values = row.getColumnValuesMap();
      boolean valid = true;
      for(String targetColumn : where.keySet()){
        if(!where.get(targetColumn).equals(values.get(columnLabel.indexOf(targetColumn)))){
          valid = false;
        }
      }
      if(valid){
        HashMap<String, String> resulatRow = new HashMap<String,String>();
        for(String targetColumn : selectedLabels){
          resulatRow.put(targetColumn, values.get(columnLabel.indexOf(targetColumn)));
        }
        res.add(resulatRow);
        if(take != -1){
          take = take - 1;
        }
      }
      if(take == 0){
        return res;
      }
    }
    return res;
  }
}
