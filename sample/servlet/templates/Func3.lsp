<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
  <lsp:output method="html"/>
  <head>
    <title>LSP Servlet test, func 3</title>
  </head>
  <body>
    <h1>LSP Servlet test, func 3</h1>
    <form>
    Box 1: 
    <input type="checkbox" name="box1">
      <lsp:attribute name="{if ($flag='foobar') then 'checked' else ''}" value="checked"/></input><br/>

    Box 2: 
    <input type="checkbox" name="box2">
      <lsp:if test="$flag='foobar'">
        <lsp:attribute name="checked" value="checked"/>
      </lsp:if></input><br/>

    </form>    
  </body>
</html>

