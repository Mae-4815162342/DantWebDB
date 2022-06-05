package model.requests;

import exception.ColumnNotExistsException;
import exception.InvalidSelectRequestException;
import exception.InvalidUpdateRequestException;
import model.Table;
import model.TableTree;

public interface SelectSchema {
    void run() throws ColumnNotExistsException, InvalidSelectRequestException, InvalidUpdateRequestException;
    void setRequest(TableTree table, boolean fromClient) throws Exception;
    void addLines(Object lines) throws Exception;
    Object getRes();
    int getLimit();
    boolean hasSelect();
    boolean hasWhere();
}
