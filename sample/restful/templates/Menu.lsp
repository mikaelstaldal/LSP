<?xml version="1.0" encoding="iso-8859-1"?>
<html xmlns:lsp="http://staldal.nu/LSP/core"
      xmlns:s="http://staldal.nu/LSP/ExtLib/Servlet"
      xmlns="http://www.w3.org/1999/xhtml">
<head><title>LSP Framework test</title></head>
<body>
<h1>LSP Framework test</h1>

<ul>
<li><a href="{s:encodeURL('Func1')}">Test 1</a></li>
<li><a href="{s:encodeURL('Forward')}">Forward to Test 1</a></li>
<li><a href="{s:encodeURL('Func2')}">Broken link to Test 2</a></li>
<li><a href="{s:encodeURL('subdir/Func2')}">Test 2</a></li>
<li><a href="{s:encodeURL('Func3')}">Test 3</a></li>
<li><a href="{s:encodeURL('Dirlist1')}">Test 5</a></li>
</ul>

<h2>Parameters</h2>

<form method="post" action="{s:encodeURL('Param1')}">
	A string: <input type="text" name="foo" value=""/><br/>
	A number: <input type="text" name="bar" value=""/><br/>
	<input type="submit" value="Submit"/>
</form>

<h2>Internationalization</h2>

<ul>
<li><a href="{s:encodeURL('Setlocale')}">Set locale to english</a></li>
<li><a href="{s:encodeURL('Func4')}">Test localization</a></li>
</ul>

</body>
</html>
