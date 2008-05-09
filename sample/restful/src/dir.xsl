<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
<xsl:output method="html"/>
<xsl:template match="/">
<html>
  <head>
    <title>Directory listing</title>
  </head>

  <body>
    <h1>Directory listing</h1>
	<ul>
		<xsl:apply-templates select="dirlist"/>
	</ul>
  </body>
</html>
</xsl:template>

<xsl:template xmlns:foo="http://foo.bar" match="foo:file">
	<li><xsl:value-of select="@filename"/></li>
</xsl:template>

</xsl:stylesheet>
