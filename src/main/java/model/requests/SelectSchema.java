package model.requests;

import java.util.HashMap;
import java.util.List;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.InvalidUpdateRequestException;
import model.Table;

public interface SelectSchema {
    void run() throws ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException;
    void setRequest(Table table, boolean fromClient) throws Exception;
    void addLines(List<HashMap<String, String>> lines) throws Exception;
    Object getRes();
    int getLimit();
    boolean hasSelect();
    boolean hasWhere();
}
