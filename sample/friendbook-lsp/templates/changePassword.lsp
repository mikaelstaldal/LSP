<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Change Password">
<p>
	You can change your password by entering it here:
</p>

<form method="post" action="changePasswordSubmit.m">
	<table border="0">
		<tr>
			<td align="right"> Old Password: </td>
			<td>
				<input value="{$model.oldPassword}" name="oldPassword" id="oldPassword" type="password"/>
			</td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['oldPassword']"/>
			</td>
		</tr>
		<tr>
			<td align="right"> New Password: </td>
			<td>
				<input value="{$model.newPassword}" name="newPassword" type="password"/>
			</td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['newPassword']"/>
			</td>
		</tr>
		<tr>
			<td align="right"> New Password Again: </td>
			<td>
				<input value="{$model.newPasswordAgain}" name="newPasswordAgain" type="password"/>
			</td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['newPasswordAgain']"/>
			</td>
		</tr>
		<tr>
			<td></td><td><input value="Change" type="submit"/></td>
		</tr>
	</table>
</form>
</root>

