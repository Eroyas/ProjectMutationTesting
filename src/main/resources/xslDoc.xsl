<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">

        <html>
            <head>
                <meta name = "viewport" content = "width=device-width"></meta>

                <!-- BOOTSTRAP -->
                <link rel = "stylesheet" href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"></link>

                <style>
                    <!-- AUTRE STYLE ICI -->
                </style>

                <!-- JQUERYcJS -->
                <script src="https://code.jquery.com/jquery-1.11.0.min.js"></script>

                <!-- HIGHCHARTS JS -->
                <script src="https://code.highcharts.com/highcharts.js"></script>
                <script src="https://code.highcharts.com/modules/exporting.js"></script>
            </head>
            <body>

                <xsl:variable name="varFailures">
                    <xsl:for-each select="root/testsuite">
                        <xsl:value-of select="@failures"/>
                    </xsl:for-each>
                </xsl:variable>

                <xsl:variable name="varTests">
                    <xsl:for-each select="root/testsuite">
                        <xsl:value-of select="@tests"/>
                    </xsl:for-each>
                </xsl:variable>

                <xsl:variable name="varSkipped">
                    <xsl:for-each select="root/testsuite">
                        <xsl:value-of select="@skipped"/>
                    </xsl:for-each>
                </xsl:variable>

                <!-- SCRIPT -->
                <script type = "text/javascript">

                    $(document).ready(function () {
                        $('#chart').highcharts({
                            chart: {
                                plotBackgroundColor: null,
                                plotBorderWidth: null,
                                plotShadow: false,
                                type: 'pie'
                            },
                            title: {
                                text: 'MUTATION TESTING'
                            },
                            tooltip: {
                                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                            },
                            plotOptions: {
                                pie: {
                                    allowPointSelect: true,
                                    cursor: 'pointer',
                                    dataLabels: {
                                        enabled: true,
                                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                        style: {
                                            color: (Highcharts.theme &amp;&amp; Highcharts.theme.contrastTextColor) || 'black'
                                        }
                                    }
                                }
                            },
                            series: [{
                                name: 'Brands',
                                colorByPoint: true,
                                data: [{
                                    name: 'Failure',
                                    y:  <xsl:value-of select="$varFailures"/>,
                                    color : 'red',
                                }, {
                                    name: 'Success',
                                    y:  <xsl:value-of select="$varTests"/>,
                                    sliced: true,
                                    selected: true,
                                    color : 'green',
                                }, {
                                    name: 'Ignored',
                                    y:  <xsl:value-of select="$varSkipped"/>,
                                    color :'yellow',
                                }]
                            }]
                        });
                    });
                </script>

                <div id="chart" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>

                <table style="border-collapse : collapse; border-spacing : 2px;">
                    <xsl:for-each select="root/testsuite">
                        <ul>Pour la classe de test <xsl:value-of select="@name"/> :
                            <li>Nombres de tests lancé : <xsl:value-of select="@tests"/></li>
                            <li>Parmi eux <xsl:value-of select="@failures"/> ont échoué..!</li>
                            <xsl:for-each select="testcase">
                                <xsl:if test="failure">
                                    <li>Le test : <xsl:value-of select="@name"/> a echoué !</li>
                                </xsl:if>
                                <xsl:if test="not(failure)">
                                    <li>Le test : <xsl:value-of select="@name"/> a réussi !</li>
                                </xsl:if>
                            </xsl:for-each>
                        </ul>
                    </xsl:for-each>
                </table>

            </body >
        </html >
    </xsl:template>

</xsl:stylesheet>