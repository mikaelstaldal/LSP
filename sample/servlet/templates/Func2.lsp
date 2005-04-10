<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
  <lsp:output method="xhtml"/>
  <head>
    <title>LSP Servlet test, func 2</title>
  </head>
  <body>
    <h1>LSP Servlet test, func 2</h1>
    
    <p>hello: <lsp:value-of select="$hello"/></p>
    
    <ol>
    <lsp:for-each select="$alist" var="ent">
      <li><lsp:value-of select="$ent"/></li>
    </lsp:for-each>    
    </ol>
    
    <h2>Food</h2>
	<table border="1">
	<tr><th>Name</th><th>Type</th><th>Colour</th></tr>
	<lsp:for-each select="$food" var="ent">
		<tr>
			<td><lsp:value-of select="$ent.name"/></td>
			<td><lsp:value-of select="$ent.type"/></td>
			<td><lsp:value-of select="$ent.colour"/></td>
		</tr>
	</lsp:for-each>
	</table>
    
  </body>
</html>

