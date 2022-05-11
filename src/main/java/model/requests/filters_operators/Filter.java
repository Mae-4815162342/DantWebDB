package model.requests.filters_operators;

import java.util.List;

public class Filter {
  private Object equals;
  private Object notequals;
  private List<Object> in;
  private List<Object> notIn;
  private double lt;
  private double lte;
  private double gt;
  private double gte;
  private String contains;
  private String startsWith;
  private String endsWith;
  
  public boolean evaluate(String value, String type){
    boolean valid = true;
    valid = (equals!=null ? (type.equals("String") ? true && (value.equals((String) equals)) : true && (Double.parseDouble(value) == ((Double) equals))) : valid);
    valid = (notequals!=null ? (type.equals("String") ? true && (!value.equals((String) notequals)) : true && (Double.parseDouble(value) != ((Double) notequals))) : valid);
    valid = (in!=null ? valid && in.contains(value) : valid);
    valid = (notIn!=null ? valid && !notIn.contains(value) : valid);
    valid = (lt!=0 ? valid && (Double.parseDouble(value) < lt) : valid);
    valid = (lte!=0 ? valid && (Double.parseDouble(value) <= lte): valid);
    valid = (gte!=0 ? valid && (Double.parseDouble(value) >= gte) : valid);
    valid = (gt!=0 ? valid && (Double.parseDouble(value) > gt) : valid);
    valid = (contains!=null ? valid && (value.contains(contains)) : valid);
    valid = (startsWith!=null ? valid && (value.startsWith(startsWith)) : valid);
    valid = (endsWith!=null ? valid && (value.endsWith(endsWith)) : valid);
    return valid;
  }
}
