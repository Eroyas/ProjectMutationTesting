<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">

        <html>
            <head>
                <meta name = "viewport" content = "width=device-width"></meta>

                <!-- BOOTSTRAP -->
                <link rel = "stylesheet" href = "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/darkly/bootstrap.min.css"></link>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

                <style>
                    <!-- AUTRE STYLE ICI -->
                </style>

                <!-- JQUERY JS -->
                <script src="https://code.jquery.com/jquery-1.11.0.min.js"></script>

                <!-- HIGHCHARTS JS -->
                <script src="https://code.highcharts.com/highcharts.js"></script>
                <script src="https://code.highcharts.com/modules/exporting.js"></script>
            </head>
            <body>

                <xsl:variable name="varTests">
                    <xsl:value-of select="sum(//testsuite/@tests)"/>
                </xsl:variable>

                <xsl:variable name="varFailures">
                    <xsl:value-of select="sum(//testsuite/@failures)"/>
                </xsl:variable>

                <xsl:variable name="varSuccess">
                    <xsl:value-of select="($varTests - $varFailures)"/>
                </xsl:variable>

                <xsl:variable name="varSkipped">
                    <xsl:value-of select="sum(//testsuite/@skipped)"/>
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
                                name: 'Tests',
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

                <div class="navbar navbar-default navbar-fixed-top">
                    <div class="container">
                        <div class="navbar-header">
                            <a href="" class="navbar-brand">MUTATION TESTING</a>
                        </div>
                        <div class="navbar-collapse collapse" id="navbar-main">
                            <ul class="nav navbar-nav">
                                <li>
                                    <!-- <a href="">Bla left</a> -->
                                </li>
                                <li>
                                    <!-- <a href="">Bla left</a> -->
                                </li>
                            </ul>

                            <ul class="nav navbar-nav navbar-right">
                                <!-- <li><a href="" target="">Bla right</a></li> -->
                            </ul>

                        </div>
                    </div>
                </div>

                <div class="container">

                    <br></br>
                    <div class="page-header" id="banner">
                        <div class="row">
                            <div class="col-lg-8 col-md-7 col-sm-6">
                                <h2>Bienvenue,</h2>
                                <p class="lead">sur votre rapport de test par mutation.</p>
                            </div>
                            <div class="col-lg-4 col-md-5 col-sm-6">
                                <br></br>
                                <br></br>
                                <div class="sponsor">
                                    <img src="http://maven-plugins.sourceforge.net/images/maven-plugins-logo.gif"></img>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="container" style="color: black; background-color: white; -moz-border-radius: 10px; -webkit-border-radius: 10px; border-radius: 10px;">

                    <div>
                        <div class="row" style="font-size: 34px; margin-bottom: 1px;">
                            <div class="col-lg-5 col-md-6 col-sm-7">
                                <p style="font-weight: bold;">Une vue d'ensemble,</p>
                                <p>voici les resultats globaux : </p>
                                <p class="text-primary">Il y a eu <xsl:value-of select="$varTests"/> tests lancé</p>
                                <p class="text-success">dont <xsl:value-of select="$varSuccess"/> fini avec succès,</p>
                                <p class="text-danger"><xsl:value-of select="$varFailures"/> fini en échec</p>
                                <p class="text-warning">et <xsl:value-of select="$varSkipped"/> ignoré.</p>
                            </div>
                            <div class="col-lg-7 col-md-6 col-sm-5">
                                <div id="chart" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>

                </div>

                <br></br>
                <div class="progress">
                    <div class="progress-bar" style="width: 100%;"></div>
                </div>

                <div class="container">

                    <table class="table table-striped table-hover ">
                        <xsl:for-each select="root/testsuite">
                            <tbody>
                                <tr>
                                    <th>
                                        <div class="row">
                                            <div class="col-lg-4 col-md-5 col-sm-6">
                                                Pour la classe de test <xsl:value-of select="@name"/> :
                                            </div>
                                            <div class="col-lg-4 col-md-5 col-sm-6">
                                                Nombres de tests lancé : <xsl:value-of select="@tests"/>
                                            </div>
                                            <div class="col-lg-4 col-md-5 col-sm-6">
                                                Parmi eux <xsl:value-of select="@failures"/> ont échoué
                                            </div>
                                        </div>
                                    </th>
                                </tr>
                                <tr>
                                    <xsl:for-each select="testcase">
                                        <xsl:if test="failure">
                                            <tr class="danger"><td>Le test : <xsl:value-of select="@name"/> a echoué !</td></tr>
                                        </xsl:if>
                                        <xsl:if test="not(failure)">
                                            <tr class="success"><td>Le test : <xsl:value-of select="@name"/> a réussi !</td></tr>
                                        </xsl:if>
                                    </xsl:for-each>
                                </tr>
                            </tbody>
                        </xsl:for-each>
                    </table>

                </div>

                <br></br>
                <div class="progress">
                    <div class="progress-bar" style="width: 100%;"></div>
                </div>

            </body >
        </html >
    </xsl:template>

</xsl:stylesheet>