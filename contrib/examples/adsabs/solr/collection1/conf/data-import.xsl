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

    
</xsl:stylesheet>
