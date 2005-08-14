<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:s="java://nu.staldal.lsp.servlet.XSLTExt"                
                exclude-result-prefixes="s">
<xsl:output method="html"/>
<xsl:param name="context"/>
<xsl:param name="pageName"/>
<xsl:template match="/">
<html>
  <head>
    <title>Extension library test</title>
  </head>

  <body>
    <h1>Using extensions from XSLT</h1>
    <p><a href="{s:encodeURL($context,'FUNC1')}">Link to FUNC1 with URLencoding</a></p>
   
    <p>foo: <xsl:value-of select="s:lang($context, 'foo')"/></p>
    <p>bar: <xsl:value-of select="s:lang($context, 'bar')"/></p>
    <p>foobar: <img alt="{s:lang($context,$pageName,'foobar')}" src="theimage.png"/></p>
   
  </body>
</html>
</xsl:template>

</xsl:stylesheet>

