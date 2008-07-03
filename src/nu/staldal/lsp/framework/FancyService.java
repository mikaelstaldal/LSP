package nu.staldal.lsp.framework;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Abstract base class for Fancy Service.
 *<p>
 * Services will be loaded and instantiated using <code>Class.forName</code> 
 * and <code>Class.newInstance</code>, so they must have a public no-arg 
 * constructor.
 *<p>
 * A new instance will be created for each request, and that instance is 
 * thrown away after the request. The {@link #execute() execute}
 * method is only invoked once per instance, so there are no thread-safety 
 * issues. It's not a problem to use instance fields.
 *<p>
 * Use the {@link Parameter} annotation on instance fields to automatically
 * populate them with HTTP request parameter values.
 *<p>
 * Use the {@link PageParameter} annotation on instance fields to automatically
 * use them as page parameters to templates.
 * 
 * @author Mikael Ståldal
 */
public abstract class FancyService extends ThrowawayService {

    @Override
    public String execute(Map<String, Object> pageParams) throws Exception {
        String template = execute();

        for (Field f : getClass().getFields()) {
            PageParameter p = f.getAnnotation(PageParameter.class);
            if (p != null) {
                String paramName = (p.value() != null && p.value().length() > 0) ? p
                        .value()
                        : f.getName();
                
                pageParams.put(paramName, f.get(this));
            }
        }
        return template;
    }

    public abstract String execute() throws Exception;

}
