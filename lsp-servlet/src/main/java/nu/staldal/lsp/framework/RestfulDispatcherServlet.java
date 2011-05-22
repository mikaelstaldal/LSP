package nu.staldal.lsp.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * Dispatcher Servlet for LSP framework with RESTful dispatching.
 *
 * Will be available in {@link javax.servlet.ServletContext} under the key
 * <code>nu.staldal.lsp.framework.DispatcherServlet</code>.
 *
 * @author Mikael StÃ¥ldal
 */
public class RestfulDispatcherServlet extends DispatcherServlet {

    public static final String EXTRA_ARGS = "ExtraArgs";
    
    @Override
    protected boolean useTemplateIfServiceIsNotFound() {
        return false;
    }    
    
    @Override
    public String dispatchService(HttpServletRequest request)
    {
        String requestPath = request.getPathInfo();
        
        List<String> extra = new ArrayList<String>(); 
        StringBuilder className = new StringBuilder();

        if (requestPath != null) {
            boolean first = true;
            for (StringTokenizer st = new StringTokenizer(requestPath, "/"); st.hasMoreTokens(); ) {
                String part = st.nextToken();
                if (part.length() == 0) {
                    continue;
                }
                if (Character.isJavaIdentifierStart(part.charAt(0)) && extra.isEmpty()) {
                    if (!first) className.append('.');
                    className.append(part);
                } else {
                    extra.add(part);
                }
                
                first = false;
            }
        }        
        request.setAttribute(EXTRA_ARGS, extra);
        
        if (className.length() == 0) {
            if (defaultService == null)
                return "";
            else                    
                return defaultService;                    
        }
        else {
            return className.toString();    
        }
    }    
    
}
