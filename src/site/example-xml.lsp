<?xml version="1.0" encoding="UTF-8"?>
<html xmlns:lsp="http://staldal.nu/LSP/core" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://staldal.nu/LSP/core"
      xmlns="http://www.w3.org/1999/xhtml">
<head><title>XML schema example</title></head>
<body>
	<lsp:value-of select="required" disable-output-escaping="true"/>

	<lsp:if test="required"/>

	<lsp:choose>
		<lsp:when test="required"/>
		<lsp:when test="required"/>
		<lsp:otherwise/>
	</lsp:choose>

	<lsp:for-each select="string" var="string" />
	<lsp:for-each select="string" var="string" status="string"/>

	<lsp:let/>
	<!-- <lsp:let var="val"/> -->

	<lsp:import file="../file.lsp"/>

	<lsp:root extend="optional"/>

	<lsp:processing-instruction name="required"/>

	<lsp:element name="required" namespace="optional"/>

	<lsp:attribute name="required" value="required" namespace="optional"/>

	<lsp:output method="html"
			version="nmtoken???" encoding="optional default=UTF-8" omit-xml-declaration="yes"
			standalone="yes" doctype-public="optional" doctype-system="optional" />

	<lsp:output
			version="nmtoken???" encoding="UTF-8" omit-xml-declaration="no"
			standalone="no" doctype-public="optional" doctype-system="optional"
			indent="yes" media-type="optional" stylesheet="optional"/>

	<lsp:include part="required"/>

	<lsp:part name="required"/>
</body>
</html>
