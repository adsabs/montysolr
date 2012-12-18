<?xml version="1.0" encoding="UTF-8"?>
<!--

This stylesheet filters out unwanted values from the Invenio MARCXML

-->

<xsl:stylesheet version="1.0" xmlns:marc="http://www.loc.gov/MARC21/slim"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes"/>
	
   <xsl:template match="node()|@*">
   	<xsl:copy>
      	<xsl:apply-templates select="node()|@*"/>
      </xsl:copy>
   </xsl:template>

   <xsl:template match="marc:datafield[@tag='260' and marc:subfield[@code='t']!='main-date']"/>

   <xsl:template match="marc:datafield[@tag='999' and marc:subfield[@code='e']!='1']"/>

   <xsl:template match="marc:datafield[@tag='856']">
      <xsl:text disable-output-escaping="yes">&lt;</xsl:text>linkfield<xsl:text disable-output-escaping="yes">&gt;</xsl:text>{'title':'<xsl:value-of select="marc:subfield[@code='y']" />', 'type':'<xsl:value-of select="marc:subfield[@code='3']" />', 'instances':'<xsl:value-of select="marc:subfield[@code='5']" />'}<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/linkfield<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
   </xsl:template>
    
</xsl:stylesheet>
