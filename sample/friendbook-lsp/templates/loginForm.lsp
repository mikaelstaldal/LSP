<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE lsp:root [
<!ENTITY nbsp "&#160;">
]>
<!-- This file is imported. Expects to have a page variable "dest" already set. -->
<lsp:root xmlns="http://www.w3.org/1999/xhtml"
          xmlns:lsp="http://staldal.nu/LSP/core">
<form action="loginSubmit.m" method="post">
	<input type="hidden" name="dest" value="{$dest}"/>
	<table border="0">
		<tr>
			<td align="right"> Login Name: </td>
			<td> <input type="text" name="name" value=""/> </td>
		</tr>
		<tr>
			<td align="right"> Password: </td>
			<td> <input type="password" name="password" value=""/> 
				 <input type="submit" value="Login"/> </td>
		</tr>
	</table>
</form>

<p>
	Would you like to <a href="signup.m">create an account</a>?
</p>
</lsp:root>

