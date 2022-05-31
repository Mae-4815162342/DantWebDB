package model.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import model.Row;
import model.Table;

public class FindUniqueSelect implements SelectSchema{
  private HashMap<String, Boolean> select;
  private HashMap<String, String> where;
  private Table table;
  private List<String> columnLabel;
  private HashMap<String,String> result;
  private Set<String> selectedLabels;

  private synchronized void handleRow(Row row, HashMap<String, String> machineRow){
    if(result != null){
      return;
    }
    boolean valid = true;
    if(row != null) {
      List<String> values = row.toList();
      for (String targetColumn : where.keySet()) {
        if (!where.get(targetColumn).equals(values.get(columnLabel.indexOf(targetColumn)))) {
          valid = false;
        }
      }
      if(valid){
        result = new HashMap<>();
        for(String targetColumn : selectedLabels){
          result.put(targetColumn, values.get(columnLabel.indexOf(targetColumn)));
        }
      }
    } else {
      result = machineRow;
    }
  }
  
  public void run() throws ColumnNotExistsException {
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
      handleRow(row, null);
      if(result != null) break;
    }
  }

  @Override
  public Object getRes() {
    return result;
  }

  @Override
  public void setRequest(Table table, boolean fromClient) throws Exception {
    this.table = table;
    if(where==null){
      throw new InvalidSelectRequestException();
    }
    if(select==null){
      selectedLabels = table.getColumns().keySet();
    }
    else{
      selectedLabels = select.keySet();
    }
    columnLabel = new ArrayList<String>(table.getColumns().keySet());
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
  public void addLines(Object lines) throws Exception {
    System.out.println(lines);
    for(HashMap<String, String> line : (List<HashMap<String, String>>) lines) {
      handleRow(null, line);
    }
  }
}
