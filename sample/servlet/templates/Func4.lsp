<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns:s="http://staldal.nu/LSP/ExtLib/Servlet"
      xmlns="http://www.w3.org/1999/xhtml">
  <lsp:output method="html"/>
  <head>
    <title>LSP Servlet test, func 4</title>
  </head>
  <body>
    <h1>LSP Servlet test, func 4 - Translation</h1>
    
    <p>foo: <s:lang key="foo"/></p>
    <p>bar: <s:lang key="bar"/></p>
    <p>foobar: <img alt="{s:lang('foobar')}" src="theimage.png"/></p>
  </body>
</html>

