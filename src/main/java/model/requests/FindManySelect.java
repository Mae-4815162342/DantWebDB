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

public class FindManySelect implements BasicSchema{
  private int limit = -1;
  private Set<String> select;
  private HashMap<String, String> where;
  private HashMap<String, String> orderBy;

  private List<HashMap<String,String>> orderResult(List<HashMap<String,String>> res, LinkedHashMap<String, String> columnLabel){
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
  }
  public List<HashMap<String,String>> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException {
    if(select==null){
      select = table.getColumns().keySet(); 
    }
    List<String> columnLabel = new ArrayList<String>(table.getColumns().keySet());
    List<HashMap<String,String>> res = new ArrayList<>();
    List<Row> lines = table.getLines().selectAll();
    for(Row row : lines){
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
        HashMap<String, String> resulatRow = new HashMap<String,String>();
        for(String targetColumn : select){
          resulatRow.put(targetColumn, values.get(columnLabel.indexOf(targetColumn)));
        }
        res.add(resulatRow);
        if(limit != -1){
          limit = limit - 1;
        }
      }
      if(limit == 0){
        break;
      }
    }
    if(orderBy!=null){
      orderResult(res, table.getColumns());
    }
    return res;
  }
}
