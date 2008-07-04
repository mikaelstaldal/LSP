package nu.staldal.lsp.framework;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Abstract base class for easy services.
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
public abstract class EasyService extends ThrowawayService {

    /**
     * Do not override this method.
     * 
     * @see #execute()
     */
    @Override
    public final String execute(Map<String, Object> pageParams) throws Exception {
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

    /**
     * Invoked for a request to this Service.
     *<p>
     * Is invoked for GET, HEAD, POST, PUT and DELETE requests. You should not treat
     * HEAD requests differently than GET requests, the framework will
     * automatically discard the body and only send the headers. The
     * {@link #requestType} field indicate the type of request.
     * See the HTTP specification for differences between GET and POST requests.
     *<p>
     * There are three choices to create the response:
     *<ol>
     *<li>Return the name of a page to display, and pass parameters to this page
     * using fields annotated with {@link PageParameter}. In this case, <code>response</code> 
     * should only be used to set headers.</li>
     *<li>Send the whole response by using <code>response</code>  
     * and return <code>null</code>. In this case the framework will not 
     * touch <code>response</code> after this method returns. This can be used if you want
     * to use {@link javax.servlet.http.HttpServletResponse#sendError(int) sendError}
     * or {@link javax.servlet.http.HttpServletResponse#sendRedirect sendRedirect}.</li>
     *<li>Return the name of an other service to forward the request to,
     * prefixed by "*". You may add attributes to <code>request</code> in order to
     * comnunicate with the other service.</li>
     *</ol>
     *<p>
     * If <code>requestType</code> is {@link #REQUEST_INCLUDE},
     * choice 2 and 3 may not be used, and <code>response</code> may not be
     * modified in any way. You may either return the name of page, or use 
     * the SAX2 {@link org.xml.sax.ContentHandler} passed as a request 
     * attribute with name "org.xml.sax.ContentHandler" and 
     * return <code>null</code>. <code>startDocument</code> and 
     * <code>endDocument</code> must not be invoked on the ContentHandler, use
     * {@link nu.staldal.lsp.ContentHandlerStartEndDocumentFilter} if this is 
     * a problem.
     *
     * @return name of the page to view, or <code>null</code> to not 
     *         use any page, or the name of an other service to forward to
     *         prefixed by "*"
     *
     * @throws Exception  may throw any Exception
     */
    public abstract String execute() throws Exception;

}
