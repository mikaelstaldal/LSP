<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Login Required">		
<p>
	You must log in first.
</p>

<lsp:let dest="$model.dest">
  <lsp:import file="loginForm.lsp"/>
</lsp:let>
</root>

