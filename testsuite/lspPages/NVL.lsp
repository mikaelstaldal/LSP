<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
<lsp:output method="xhtml" encoding="UTF-8" indent="no"/>
<p><lsp:value-of select="nvl('foo','bar')"/></p>
<p class="{nvl(17,'bar')}">doh</p>
<p><lsp:value-of select="nvl($thisIsNotNull,'bar')"/></p>
<p><lsp:value-of select="nvl($thisIsNull,'bar')"/></p>
</html>
