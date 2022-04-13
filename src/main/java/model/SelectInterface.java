package model;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;

public interface SelectInterface {
  public Object run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException;
}
