<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html [
<!ENTITY nbsp "&#160;">
]>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
    <lsp:output method="html" encoding="UTF-8"/>
	<head>
		<title>Welcome</title>
		<link rel="stylesheet" href="stylesheet.css" type="text/css" />
	</head>

	<body>
		<table cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td class="pageTitle" colspan="2"> <h1>Welcome</h1> </td>
			</tr>
			<tr align="center" style="text-align: center">
				<td class="navigationTop"> <a class="nav" href="welcome.m">Login</a> </td>
				<td class="navigationTop"> <a class="nav" href="signup.m">Sign Up</a> </td>
			</tr>
		</table>

        <p>
            Welcome to the Friendbook example.  This is a simple contact-list
            application which demonstrates how to create a membership-based
            website with Maverick.
        </p>
        
        <lsp:let dest="'friends.m'">
          <lsp:import file="loginForm.lsp"/>
        </lsp:let>
	</body>
</html>

