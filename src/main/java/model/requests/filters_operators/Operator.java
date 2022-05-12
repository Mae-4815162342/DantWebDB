package model.requests.filters_operators;

import java.util.LinkedHashMap;

import model.Row;

public interface Operator {
  public boolean evaluate(Row row, LinkedHashMap<String, String> columns);
}
