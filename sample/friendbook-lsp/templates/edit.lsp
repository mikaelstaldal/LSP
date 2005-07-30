<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Edit Your Info">
<lsp:root xmlns="http://www.w3.org/1999/xhtml">
<p>
	Please make your changes:
</p>

<form action="editSubmit.m" method="post">
	<table border="0">
		<tr>
			<td class="editTitle">First Name:</td>
			<td><input name="firstName" value="{$model.firstName}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['firstName']"/>
			</td>
		</tr>
		<tr>
			<td class="editTitle">Last Name:</td>
			<td><input name="lastName" value="{$model.lastName}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['lastName']"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td class="editTitle">Address:</td>
			<td><input type="text" name="addrLine1" value="{$model.addrLine1}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['addrLine1']"/>
			</td>
		</tr>
		<tr>
			<td class="editTitle">(line 2)</td>
			<td><input type="text" name="addrLine2" value="{$model.addrLine2}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['addrLine2']"/>
			</td>
		</tr>
		<tr>
			<td class="editTitle">State:</td>
			<td><input type="text" name="addrState" value="{$model.addrState}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['addrState']"/>
			</td>
		</tr>
		<tr>
			<td class="editTitle">City:</td>
			<td><input type="text" name="addrCity" value="{$model.addrCity}"/></td>
			<td class="errorText">
				<lsp:value-of select="$model.errors['addrCity']"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="3" style="font-weight: bold">Phone numbers:</td>
		</tr>

		<!-- list phone numbers -->
		<lsp:for-each var="phone" status="s" select="$model.phoneList">
			<tr>
				<td>del? <input type="checkbox" name="del_phone{$s.index}"/></td>
				<td>
					<input style="width: 250px;"
						type="text"
						name="phone{$s.index}"
						value="{$phone}"/>
				</td>
				<td class="errorText">
					<lsp:let key="concat('phone','$s.index')">
					  <lsp:value-of select="$model.errors[$key]"/>
                    </lsp:let>
				</td>
			</tr>
		</lsp:for-each>

		<tr>
			<td>Add Number</td>
			<td>
				<input style="width: 250px;" type="text" name="phone{count($model.phoneList)+1}" value=""/>
			</td>
		</tr>

		<tr>
			<td colspan="3" style="font-weight: bold">Email Addresses:</td>
		</tr>

		<!-- list emails -->
		<lsp:for-each var="email" status="s" select="$model.emailList">
			<tr>
				<td>del? <input type="checkbox" name="del_email{$s.index}"/></td>
				<td>
					<input style="width: 250px;"
						type="text"
						name="email{$s.index}"
						value="{$email}"/>
				</td>
				<td class="errorText">
					<lsp:let key="concat('email',$s.index)">
					  <lsp:value-of select="$model.errors[$key]"/>
                    </lsp:let>
				</td>
			</tr>
		</lsp:for-each>
		<tr>
			<td>Add Email</td>
			<td><input style="width: 250px;" type="text" name="email{count($model.phoneList)+1}" value=""/></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Save"/></td>
		</tr>
	</table>
</form>
</lsp:root>
</root>

