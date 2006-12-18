package test.services;

import java.util.Map;

import nu.staldal.lsp.framework.*;

public class Param1 extends ThrowawayService
{
    @Parameter
    public String foo;

    @Parameter
    public int bar;

    @Override
    public String execute(Map<String, Object> pageParams) throws Exception
    {
        pageParams.put("foo", foo.toUpperCase());
        pageParams.put("bar", bar*2);
        
        return "Param1";
    }

}
