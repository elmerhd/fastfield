package open.elmerhd.fastfield;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author elmerhd
 */
@ApplicationPath("api")
public class FastfieldApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(open.elmerhd.fastfield.ImageGetter.class);
        return classes;
    }
}
