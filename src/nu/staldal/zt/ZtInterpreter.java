package nu.staldal.zt;

import java.util.Map;
import java.util.Properties;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import nu.staldal.lsp.LSPPage;
import nu.staldal.util.Environment;

/**
 * ZeroTemplate interpreter.
 *
 * @author Mikael Ståldal
 */
class ZtInterpreter implements LSPPage {

    private final ZtElement compiledTree;
    private final String pageName;
    private final String[] compileDependentFiles;
    private final long timeCompiled;
    private final Properties outputProperties;
    
    public ZtInterpreter(ZtElement compiledTree, String pageName, String[] compileDependentFiles, 
            long timeCompiled, Properties outputProperties) {
        this.compiledTree = compiledTree;
        this.pageName = pageName;
        this.compileDependentFiles = compileDependentFiles;
        this.timeCompiled = timeCompiled;
        this.outputProperties = outputProperties;
    }

    public void execute(ContentHandler sax, Map<String,Object> params,
            Object extContext) throws SAXException {
        Environment<String,Object> env = new Environment<String,Object>(params);
        compiledTree.toSAX(sax, env);        
    }

    public String[] getCompileDependentFiles() {
        return compileDependentFiles;
    }

    public Properties getOutputProperties() {
        return outputProperties;
    }

    public String getPageName() {
        return pageName;
    }

    public long getTimeCompiled() {
        return timeCompiled;
    }

    public boolean isCompileDynamic() {
        return false;
    }

}
