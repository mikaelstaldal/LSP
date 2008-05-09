package nu.staldal.lsp.framework;

import javax.servlet.http.HttpServletRequest;

/**
 * Dispatcher Servlet for LSP framework with RESTful dispatching.
 *
 * Will be available in {@link javax.servlet.ServletContext} under the key
 * <code>nu.staldal.lsp.framework.DispatcherServlet</code>.
 *
 * @author Mikael Ståldal
 */
public class RestfulDispatcherServlet extends DispatcherServlet {

    @Override
    protected boolean useTemplateIfServiceIsNotFound() {
        return false;
    }    
    
    @Override
    public String dispatchService(HttpServletRequest request)
    {
        String requestPath = request.getPathInfo();
        
        if (requestPath == null || requestPath.length() == 0)
        {
            if (defaultService == null)
                return "";
            else                    
                return defaultService;                    
        }

        int startPos = requestPath.startsWith("/") ? 1 : 0;

        String ret = requestPath.substring(startPos);
        if (ret.length() == 0)
        {
            if (defaultService == null)
                return "";
            else                    
                return defaultService;                    
        }
        else
        {
            return ret.replace('/', '.');    
        }
    }    
    
}
