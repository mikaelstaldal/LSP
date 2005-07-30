<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Sign Up">
<lsp:root xmlns="http://www.w3.org/1999/xhtml">
<p>
	To create an account, just fill out this form:
</p>

<form action="signupSubmit.m" method="post">
	<table border="0">
		<tr>
			<td align="right"> Login Name: </td>
			<td> <input type="text" name="loginName" value="{$mymodel.loginName}" /> </td>
			<td class="errorText"><lsp:value-of select="$mymodel.errors['loginName']"/></td>
		</tr>
		<tr>
			<td align="right"> Password: </td>
			<td> <input type="password" name="password" value="{$mymodel.password}"/> </td>
			<td class="errorText"><lsp:value-of select="$mymodel.errors['password']"/></td>
		</tr>
		<tr>
			<td align="right"> Password Again: </td>
			<td> <input type="password" name="passwordAgain" value="{$mymodel.passwordAgain}"/> </td>
			<td class="errorText"><lsp:value-of select="$mymodel.errors['passwordAgain']"/></td>
		</tr>
		<tr>
			<td></td>
			<td> <input type="submit" value="Signup"/> </td>
		</tr>
	</table>
</form>
</lsp:root>
</root>

