package nu.staldal.lsp.framework;

import java.util.Map;

public class ThrowawayServiceWithParam extends ThrowawayService
{
    @Parameter
    public String param = "defaultValue";

    @Parameter
    public String notUsedParam = "defaultValue";

    @Parameter("strangeParam")
    public String paramWithStrangeName;
    
    @Mandatory @Parameter
    public String mandatoryParam;
    
    @Parameter
    public int intParam;
    
    @Parameter
    public double doubleParam;

    @Parameter
    public int notUsedIntParam;

    @Parameter
    public Integer integerParam;

    @Parameter
    public Integer notUsedIntegerParam;

    @Parameter
    public char charParam;

    @Parameter
    public MyEnum enumParam;

    @Override
    public String execute(Map<String, Object> pageParams) throws Exception
    {
        pageParams.put("msg", "ThrowawayServiceWithParam:"
                + " mandatoryParam="+mandatoryParam 
                + " param="+param
                + " notUsedParam="+notUsedParam
                + " paramWithStrangeName="+paramWithStrangeName
                + " intParam="+String.valueOf(intParam)
                + " doubleParam="+String.valueOf(doubleParam)
                + " notUsedIntParam="+String.valueOf(notUsedIntParam)
                + " integerParam="+String.valueOf(integerParam)
                + " notUsedIntegerParam="+String.valueOf(notUsedIntegerParam)
                + " charParam="+charParam
                + " enumParam="+enumParam);
        
        return "TestPage";
    }

}
