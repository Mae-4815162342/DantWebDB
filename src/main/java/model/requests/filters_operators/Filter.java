package model.requests.filters_operators;

import java.util.List;

public class Filter {
  private Object equals;
  private Object notequals;
  private List<Object> in;
  private List<Object> notIn;
  private Double lt;
  private Double lte;
  private Double gt;
  private Double gte;
  private String contains;
  private String startsWith;
  private String endsWith;
  public Object parseObject(String value, String type){
    switch(type){
      case "int":
        return Double.parseDouble(value);
      default:
        return value;
    }
  }
  public boolean evaluate(String value, String type){
    boolean valid = true;
    valid = (equals!=null ? (type.equals("String") ? true && (value.equals((String) equals)) : true && (Double.parseDouble(value) == ((Double) equals))) : valid);
    valid = (notequals!=null ? (type.equals("String") ? true && (!value.equals((String) notequals)) : true && (Double.parseDouble(value) != ((Double) notequals))) : valid);
    valid = (in!=null ? valid && in.contains(parseObject(value, type)) : valid);
    valid = (notIn!=null ? valid && !notIn.contains(parseObject(value, type)) : valid);
    valid = (lt!=null ? valid && Double.compare(Double.parseDouble(value),lt) < 0 : valid);
    valid = (lte!=null ? valid && Double.compare(Double.parseDouble(value),lte) <= 0: valid);
    valid = (gte!=null ? valid && Double.compare(Double.parseDouble(value),gte) >= 0 : valid);
    valid = (gt!=null ? valid && Double.compare(Double.parseDouble(value),gt) > 0 : valid);
    valid = (contains!=null ? valid && (value.contains(contains)) : valid);
    valid = (startsWith!=null ? valid && (value.startsWith(startsWith)) : valid);
    valid = (endsWith!=null ? valid && (value.endsWith(endsWith)) : valid);
    return valid;
  }
}
