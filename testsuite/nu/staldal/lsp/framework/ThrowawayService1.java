package nu.staldal.lsp.framework;

import java.util.Map;

public class ThrowawayService1 extends ThrowawayService
{
    private int instanceCounter = 0;

    @Override
    public String execute(Map<String, Object> pageParams) throws Exception
    {
        instanceCounter++;
        
        pageParams.put("msg", "ThrowawayService1: servletPath=" + request.getServletPath() + " instanceCounter="+instanceCounter);
        
        return "TestPage";
    }

}
