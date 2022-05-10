package storage.Columns;

import org.apache.james.mime4j.field.datetime.DateTime;
import storage.Columns.Column;

public class DatetimeColumn implements Column<DateTime> {
    @Override
    public DateTime equal(DateTime value) {
        return null;
    }

    @Override
    public DateTime notequals(DateTime value) {
        return null;
    }

    @Override
    public DateTime gt(DateTime value) {
        return null;
    }

    @Override
    public DateTime gte(DateTime value) {
        return null;
    }

    @Override
    public DateTime lt(DateTime value) {
        return null;
    }

    @Override
    public DateTime lte(DateTime value) {
        return null;
    }
}
