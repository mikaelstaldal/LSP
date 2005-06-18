<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
<xsl:output method="html"/>
<xsl:template match="/">
<html>
	<head>
		<title><xsl:value-of select="/root/@title"/></title>
		<link rel="stylesheet" href="stylesheet.css" type="text/css" />
	</head>

	<body>
		<table cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td class="pageTitle"> <h1><xsl:value-of select="/root/@title"/></h1> </td>
			</tr>
		</table>

		<div><br/></div>

		<table width="100%" cellspacing="0" cellpadding="5" border="0">
			<tr>
				<td width="1%" align="left" valign="top" class="navigationAll">
					<div class="navigationAll">
						<a class="nav" href="friends.m"> Friends </a>

						<br/>
						<br/>

						<a class="nav" href="edit.m"> Edit Info </a>

						<br/>
						<br/>

						<a class="nav" href="changePassword.m"> Change Password </a>

						<br/>
						<br/>

						<a class="nav" href="logout.m"> Logout </a>
					</div>
				</td>

				<td align="left" valign="top">
					<xsl:copy-of select="/root/*"/> 
				</td>
			</tr>
		</table>

	</body>
</html>
</xsl:template>

</xsl:stylesheet>

