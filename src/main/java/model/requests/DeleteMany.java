package model.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidUpdateRequestException;
import model.Row;
import model.Table;

public class DeleteMany implements BasicSchema {
  private HashMap<String, String> data;
  private HashMap<String, String> where;

  private Boolean handleRow(Table table, Row row, List<String> columnLabel, Set<String> updatedLabels){
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
    }
    return valid;
  }

  @Override
  public Object run(Table table) throws ColumnNotExistsException, InvalidUpdateRequestException {
    if(where==null || data==null){
      throw new InvalidUpdateRequestException();
    }
    Set<String> updatedLabels = data.keySet();
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<Row> lines = table.getLines().selectAll();
    Boolean found = false;
    for(Row row : lines){
      found = found || handleRow(table, row, columnLabel, updatedLabels);
    }
    if(found){
      return "Deletions made successfully";
    }
    return "Unfound row, please specify an existing row";
  }
}
