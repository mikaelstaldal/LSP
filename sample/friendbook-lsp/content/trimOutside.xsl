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
				<td class="pageTitle" colspan="2"> <h1><xsl:value-of select="/root/@title"/></h1> </td>
			</tr>
			<tr align="center" style="text-align: center">
				<td class="navigationTop"> <a class="nav" href="welcome.m">Login</a> </td>
				<td class="navigationTop"> <a class="nav" href="signup.m">Sign Up</a> </td>
			</tr>
		</table>

		<xsl:copy-of select="/root/*"/>
	</body>
</html>
</xsl:template>

</xsl:stylesheet>

