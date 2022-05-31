package model.requests;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.InvalidUpdateRequestException;
import model.Table;

public interface BasicSchema {
  Object run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException;
}
