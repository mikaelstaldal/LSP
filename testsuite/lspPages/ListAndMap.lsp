<?xml version="1.0" encoding="iso-8859-1"?>
<root xmlns:lsp="http://staldal.nu/LSP/core">
<h1><lsp:value-of select="$lm.foo"/></h1>
<lsp:for-each select="$lm" var="item">
<p><lsp:value-of select="$item"/></p>
</lsp:for-each>
<h1><lsp:value-of select="$lm.bar"/></h1>
</root>
