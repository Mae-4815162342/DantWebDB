package model;

import java.util.HashMap;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;

public interface SelectInterface {

  public HashMap<String,String> run(Table table) throws ColumnNotExistsException, InvalidSelectRequestException;
}
