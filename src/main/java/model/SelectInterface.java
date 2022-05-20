package model;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.WrongGroupBy;

public interface SelectInterface {
  public Object run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException, WrongGroupBy;
  public Boolean hasSelect();
  public Boolean hasWhere();
  public Boolean hasGroupBy();
  public int getLimit();
}
