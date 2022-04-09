import exception.RuntimeExceptionMapper;
import filter.GsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import endpoints.*;

@ApplicationPath("")
public class App extends Application {
    public static byte[] data;
    public static List<String> headers = new ArrayList<String>();

    @Override
    public Set<Object> getSingletons() {
        Set<Object> sets = new HashSet<>(1);
        sets.add(new TableEndpoint());
        sets.add(new TableEndpoint());
        sets.add(new Create());
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
