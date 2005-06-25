<?xml version="1.0" encoding="iso-8859-1"?>
<lsp:root 
      xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns:s="http://staldal.nu/LSP/ExtLib/Servlet"
      xmlns="http://www.w3.org/1999/xhtml">
    <h3>This is included</h3>
    
    <p>Include attribute: <lsp:value-of select="$foo"/></p>
  
    <hr/>
    <s:include name="Include2" myattr2="Very included!"/>
    <hr/>    
    
</lsp:root>

