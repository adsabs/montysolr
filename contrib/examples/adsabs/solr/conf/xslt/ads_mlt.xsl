<?xml version='1.0' encoding='UTF-8'?>

<!-- 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 -->

<!-- 
  Simple transform of Solr query results to HTML
 -->
<xsl:stylesheet version='1.0'
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
>

  <xsl:output media-type="text/html" encoding="UTF-8"/> 
  
  <xsl:variable name="title" select="concat('Solr search results + MLT (',response/result/@numFound,' documents)')"/>
  <xsl:variable name="query" select="concat('Query: ', //lst[@name='params']/str[@name='q']/text())"/>
  
  <xsl:template match='/'>
    <html>
      <head>
        <title><xsl:value-of select="$title"/></title>
        <xsl:call-template name="css"/>
      </head>
      <body>
        <h1><xsl:value-of select="$query"/></h1>
        <h3><xsl:value-of select="$title"/></h3>
        <ul>
            <li>Display Fields (fl): <xsl:value-of select="//lst[@name='params']/str[@name='fl']/text()"/></li>
            <li>MLT field (mlt.fl): <xsl:value-of select="//lst[@name='params']/str[@name='mlt.fl']/text()"/></li>
        </ul>
        <xsl:apply-templates select="response/result/doc"/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="doc">
    <xsl:variable name="pos" select="position()"/>
    <div class="doc">
      <table> 
        <xsl:apply-templates>
          <xsl:with-param name="pos"><xsl:value-of select="$pos"/></xsl:with-param>
          <xsl:with-param name="debug_mode"><xsl:value-of select="'exp'"/></xsl:with-param>
        </xsl:apply-templates>
        <xsl:if test="boolean(//lst[@name='moreLikeThis'])">
        <tr>
            <td colspan="2">
                <blockquote>
                <h5>MLT</h5>
                <xsl:for-each select="//lst[@name='moreLikeThis']/result[position()=$pos]/doc">
                    <div class="mlt-doc">
                    <table>
                    <xsl:variable name="mlt_pos" select="position()"/>
                    <xsl:apply-templates>
                      <xsl:with-param name="pos"><xsl:value-of select="$pos"/></xsl:with-param>
                      <xsl:with-param name="mlt_pos"><xsl:value-of select="$mlt_pos"/></xsl:with-param>
                      <xsl:with-param name="debug_mode"><xsl:value-of select="'mlt'"/></xsl:with-param>
                    </xsl:apply-templates>
                    </table>
                    </div>
                </xsl:for-each>
                </blockquote>
            </td>
        </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="doc/*[@name='score']" priority="100">
    <xsl:param name="pos"></xsl:param>
    <xsl:param name="mlt_pos"></xsl:param>
    <xsl:param name="debug_mode"></xsl:param>
    <tr>
      <td class="name">
        <xsl:value-of select="@name"/>
      </td>
      <td class="value">
        <xsl:value-of select="."/>

        <xsl:if test="boolean(//lst[@name='explain'])">
            <xsl:variable name="div_id">
                <xsl:choose>
                    <xsl:when test="$debug_mode='exp'">
                        <xsl:value-of select="concat($debug_mode,'-',$pos)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="concat($debug_mode,'-',$pos,'-',$mlt_pos)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
          [<xsl:element name="a">
            <!-- can't allow whitespace here -->
            <xsl:attribute name="href">javascript:toggle("<xsl:value-of select="$div_id" />");</xsl:attribute>?</xsl:element>]
          <br/>
          <xsl:element name="div">
            <xsl:attribute name="class"><xsl:value-of select="$debug_mode"/></xsl:attribute>
            <xsl:attribute name="id">
              <xsl:value-of select="$div_id" />
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="$debug_mode='exp'">
                    <xsl:value-of select="//lst[@name='debug']/lst[@name='explain']/str[position()=$pos]"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="//lst[@name='debug']/lst[@name='moreLikeThis']/lst[position()=$pos]/lst[@name='explain']/str[position()=$mlt_pos]"/>
                </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:if>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="doc/arr" priority="100">
    <tr>
      <td class="name">
        <xsl:value-of select="@name"/>
      </td>
      <td class="value">
        <xsl:for-each select="*">
          <xsl:value-of select="."/>,
        </xsl:for-each>
      </td>
    </tr>
  </xsl:template>


  <xsl:template match="doc/*">
    <tr>
      <td class="name">
        <xsl:value-of select="@name"/>
      </td>
      <td class="value">
        <xsl:value-of select="."/>
      </td>
    </tr>
  </xsl:template>

  <xsl:template name="css">
    <script>
      function toggle(id) {
        var obj = document.getElementById(id);
        obj.style.display = (obj.style.display != 'block') ? 'block' : 'none';
      }
    </script>
    <style type="text/css">
      body { font-family: "Lucida Grande", sans-serif }
      td.name { font-style: italic; font-size:80%; width:100px }
      td { vertical-align: top; }
      ul { margin: 0px; margin-left: 1em; padding: 0px; }
      .note { font-size:80%; }
      .doc { margin-top: 1em; border-top: solid grey 1px; }
      .mlt-doc { margin-top: .5em; border-top: solid grey 1px; }
      .exp { display: none; font-family: monospace; white-space: pre; }
      .mlt { display: none; font-family: monospace; white-space: pre; }
    </style>
  </xsl:template>

</xsl:stylesheet>
