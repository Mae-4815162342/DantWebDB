package model.requests.filters_operators;

import java.util.List;

public class Filter {
  private Object equals;
  private Object notequals;
  private List<Object> in;
  private List<Object> notIn;
  private int lt;
  private int lte;
  private int gt;
  private int gte;
  private String contains;
  private String startsWith;
  private String endsWith;

  public boolean evaluate(String value, String type){
    boolean valid = true;
    if(equals!=null){
      valid = (type.equals("String") ? true && (value.equals(equals)) : true && (value == equals));
    }
    if(notequals!=null){
      valid = (type.equals("String") ? true && (!value.equals(equals)) : true && (value != equals));
    }
    if(in!=null){
      valid = valid && in.contains(value);
    }
    if(notIn!=null){
      valid = valid && !in.contains(value);
    }
    if(type=="int" && lt!=0){
      valid = valid && (Integer.parseInt(value) < lt);
    }
    if(type=="int" && lte!=0){
      valid = valid && (Integer.parseInt(value) <= lt);
    }
    if(type=="int" && gt!=0){
      valid = valid && (Integer.parseInt(value) > lt);
    }
    if(type=="int" && gte!=0){
      valid = valid && (Integer.parseInt(value) >= lt);
    }
    if(type=="String" && contains!=null){
      valid = valid && (value.contains(contains));
    }
    if(type=="String" && startsWith!=null){
      valid = valid && (value.startsWith(startsWith));
    }
    if(type=="String" && endsWith!=null){
      valid = valid && (value.endsWith(endsWith));
    }
    return valid;
  }
}
