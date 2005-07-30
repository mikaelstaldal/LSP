<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Login Failed">
<lsp:root xmlns="http://www.w3.org/1999/xhtml">
<p>
	<span class="errorText">You have entered an invalid login name or bad password.</span>
</p>

<lsp:let dest="$model.dest">
  <lsp:import file="loginForm.lsp"/>
</lsp:let>
</lsp:root>
</root>
