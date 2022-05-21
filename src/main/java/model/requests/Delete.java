package model.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidUpdateRequestException;
import model.Row;
import model.Table;

public class Delete implements BasicSchema {
  private HashMap<String, String> data;
  private HashMap<String, String> where;

  private boolean handleRow(Table table, Row row, Set<String> updatedLabels, List<String> columnLabel){
      List<String> values = row.toList();
      boolean valid = true;
      for(String targetColumn : where.keySet()){
        if(!where.get(targetColumn).equals(values.get(columnLabel.indexOf(targetColumn)))){
          valid = false;
        }
      }
      if(valid){
        List<String> newRow = row.toList();
        for(String targetColumn : updatedLabels){
          newRow.set(columnLabel.indexOf(targetColumn), data.get(targetColumn));
        }
        table.deleteEntry(row);
        return true;
      }
      return false;
  }
  
  @Override
  public Object run(Table table) throws ColumnNotExistsException, InvalidUpdateRequestException {
    if(where==null || data==null){
      throw new InvalidUpdateRequestException();
    }
    Set<String> updatedLabels = data.keySet();
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      if(handleRow(table, row, updatedLabels, columnLabel)){
        return "Deletion made successfully";
      }
    }
    return "Unfound row, please specify an existing row";
  }
}
