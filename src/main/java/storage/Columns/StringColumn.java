package storage.Columns;

import storage.Columns.Column;

import java.util.ArrayList;

public class StringColumn implements Column<String> {
    public ArrayList<String> values;

    public StringColumn(){
        values=new ArrayList<String>();
    }


    @Override
    public String equal(String value) {
        return null;
    }

    @Override
    public String notequals(String value) {
        return null;
    }

    @Override
    public String gt(String value) {
        return null;
    }

    @Override
    public String gte(String value) {
        return null;
    }

    @Override
    public String lt(String value) {
        return null;
    }

    @Override
    public String lte(String value) {
        return null;
    }

    public int[] in(String value){
        return null;
    }
}
