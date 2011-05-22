package nu.staldal.lsp.framework;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Service1 implements Service
{
    private int init = 0;
    private int instanceCounter = 0;
    
    public void init(ServletContext context) throws ServletException
    {
        init++;
    }
    
    public String execute(HttpServletRequest request,
            HttpServletResponse response, Map<String, Object> pageParams,
            int requestType) throws ServletException, IOException
    {
        instanceCounter++;
        
        pageParams.put("msg", "Service1: servletPath=" + request.getServletPath() + " init="+init + " instanceCounter="+instanceCounter);
        
        return "TestPage";
    }

    public void destroy()
    {
        init = -1;
    }

}
