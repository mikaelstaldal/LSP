<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns:s="http://staldal.nu/LSP/ExtLib/Servlet"
      xmlns="http://www.w3.org/1999/xhtml">
  <lsp:output method="html"/>
  <head>
    <title>LSP Servlet test, func 1</title>
  </head>
  <body>
    <h1>LSP Servlet test, func 1</h1>
    
    <p>string: <lsp:value-of select="$str"/></p>
    <p>number: <lsp:value-of select="$num"/></p>

    <p><a href="{s:encodeURL('Func2.s')}">Shortcut to Func2</a></p>
    
    <h2>Include</h2>
    <hr/>
    <s:include name="Include" myattr="Include attribute value"/>
    <hr/>    
  </body>
</html>

