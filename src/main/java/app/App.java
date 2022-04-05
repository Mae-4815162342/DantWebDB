package app;

import exception.RuntimeExceptionMapper;
import filter.GsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationPath("")
public class App extends Application {
    /*TODO replace data by database instance*/
    public static List<Map<String, String>> data;
    public static List<String> headers = new ArrayList<String>();
    @Override
    public Set<Object> getSingletons() {
        Set<Object> sets = new HashSet<>(1);
        sets.add(new TestEndpoint());
        sets.add(new CreateTable());
        sets.add(new Create());
        sets.add(new Table());
        return sets;
    }
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> sets = new HashSet<>(1);
        sets.add(GsonProvider.class);
        sets.add(RuntimeExceptionMapper.class);
        return sets;
    }
}
