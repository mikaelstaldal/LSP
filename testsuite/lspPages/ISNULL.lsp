<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
<lsp:output method="xhtml" encoding="UTF-8" indent="no"/>
<p><lsp:value-of select="isnull($thisIsNotNull)"/></p>
<p><lsp:value-of select="isnull($thisIsNull)"/></p>
<p><lsp:value-of select="if (isnull($thisIsNotNull)) then 'isNull' else 'isNotNull'"/></p>
<p><lsp:value-of select="if (isnull($thisIsNull)) then 'isNull' else 'isNotNull'"/></p>
</html>
