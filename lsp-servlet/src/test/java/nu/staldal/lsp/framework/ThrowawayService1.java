package nu.staldal.lsp.framework;

public class ThrowawayService1 extends EasyService
{
    private int instanceCounter = 0;
    
    @PageParameter
    public String msg;

    @Override
    public String execute() throws Exception
    {
        instanceCounter++;
        
        msg = "ThrowawayService1: servletPath=" + request.getServletPath() + " instanceCounter="+instanceCounter;
        
        return "TestPage";
    }

}
