<?xml version="1.0" encoding="iso-8859-1"?>
<root xmlns:lsp='http://staldal.nu/LSP/core'>
<lsp:output encoding='UTF-8'/>
<ul xml:space='preserve'>
<lsp:for-each select='seq(1,10)' var='ent'>
<li><lsp:value-of select='$ent'/></li>
</lsp:for-each>
</ul>
</root>
