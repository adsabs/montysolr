<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:marc="http://www.loc.gov/MARC21/slim"
 xmlns="http://www.loc.gov/MARC21/slim"
 >
 <xsl:output omit-xml-declaration="yes" indent="yes"/>
 <xsl:strip-space elements="*"/>


   <xsl:template match="node()|@*">
     <xsl:copy>
       <xsl:apply-templates select="node()|@*"/>
     </xsl:copy>
   </xsl:template>

   <!-- make sure every author has email (m) /affiliation (u) - even if just an empty string -->
   <xsl:template match="marc:datafield[@tag='100' and not(marc:subfield[@code='u'])]">
	  <xsl:copy>
	   <xsl:apply-templates select="@*"/>
		 <subfield code="u">-</subfield>
	   <xsl:apply-templates select="node()"/>
	  </xsl:copy>
  </xsl:template>
   <xsl:template match="marc:datafield[@tag='100' and not(marc:subfield[@code='m'])]">
	  <xsl:copy>
	   <xsl:apply-templates select="@*"/>
		 <subfield code="m">-</subfield>
	   <xsl:apply-templates select="node()"/>
	  </xsl:copy>
  </xsl:template>

  <xsl:template match="marc:datafield[@tag='700' and not(marc:subfield[@code='u'])]">
	  <xsl:copy>
	   <xsl:apply-templates select="@*"/>
		 <subfield code="u">-</subfield>
	   <xsl:apply-templates select="node()"/>
	  </xsl:copy>
  </xsl:template>
   <xsl:template match="marc:datafield[@tag='700' and not(marc:subfield[@code='m'])]">
	  <xsl:copy>
	   <xsl:apply-templates select="@*"/>
		 <subfield code="m">-</subfield>
	   <xsl:apply-templates select="node()"/>
	  </xsl:copy>
  </xsl:template>
 

</xsl:stylesheet>
