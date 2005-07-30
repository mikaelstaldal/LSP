<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE root [
<!ENTITY nbsp "&#160;">
]>
<root xmlns:lsp="http://staldal.nu/LSP/core"
      title="Friends">
<lsp:root xmlns="http://www.w3.org/1999/xhtml">
<p>
	Here is a list of all of your friends:
</p>

<table cellspacing="1" cellpadding="5" border="0" align="center">
	<tr>
		<th class="friendTitle">Name</th>
		<th class="friendTitle">Address</th>
		<th class="friendTitle">Phone Numbers</th>
		<th class="friendTitle">Email Addresses</th>
	</tr>
	<lsp:for-each var="friend" select="$model.friends">
      <lsp:let styleAttrs="if ($friend.login = $model.myLogin) then 'background-color: yellow' else ''">
		<tr>
			<td class="friendValue" style="{$styleAttrs}" align="left" valign="top">
				<lsp:value-of select="$friend.lastName"/>,&nbsp;<lsp:value-of select="$friend.firstName"/>
				<lsp:if test="$friend.login=$model.myLogin">
					<br/>(<a href="edit.m">Edit</a>)
				</lsp:if>
			</td>
			<td class="friendValue" style="{$styleAttrs}" align="left" valign="top">
				<!-- check for address information before processing -->
				
				<lsp:choose>
					<lsp:when test="$friend.address">
						<!-- we use the test to exclude extra line breaks -->
						
						<lsp:if test="$friend.address.addressLine1">
							<lsp:value-of select="$friend.address.addressLine1"/><br/>
						</lsp:if>
						<lsp:if test="$friend.address.addressLine2">
							<lsp:value-of select="$friend.address.addressLine2"/><br/>
						</lsp:if>
						<lsp:if test="$friend.address.city">
							<lsp:value-of select="$friend.address.city"/><br/>
						</lsp:if>
						<lsp:if test="$friend.address.state">
							<lsp:value-of select="$friend.address.state"/><br/>
						</lsp:if>
					</lsp:when>
					<lsp:otherwise>
						&nbsp;
					</lsp:otherwise>
				</lsp:choose>
			</td>
			<td class="friendValue" style="{$styleAttrs}" align="left" valign="top">
				<!-- check for phone information before processing -->

				<lsp:choose>
					<lsp:when test="$friend.phoneList">
						<lsp:for-each var="phone" select="$friend.phoneList">
							<lsp:value-of select="$phone"/><br/>
						</lsp:for-each>
					</lsp:when>
					<lsp:otherwise>
						&nbsp;
					</lsp:otherwise>
				</lsp:choose>
			</td>
			<td class="friendValue" style="{$styleAttrs}" align="left" valign="top">
				<lsp:choose>
					<lsp:when test="$friend.emailList">
						<lsp:for-each var="email" select="$friend.emailList">
							<lsp:value-of select="$email"/><br/>
						</lsp:for-each>
					</lsp:when>
					<lsp:otherwise>
						&nbsp;
					</lsp:otherwise>
				</lsp:choose>
			</td>
		</tr>
      </lsp:let>
	</lsp:for-each>
</table>
</lsp:root>
</root>

