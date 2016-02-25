package fr.unice.polytech.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by Eroyas on 24/02/16.
 */
@Mojo(name = "generate-report", defaultPhase = LifecyclePhase.PACKAGE)
public class ReportMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            System.out.println("#### HTML ####\n...");
            TransformerFactory factory = TransformerFactory.newInstance();

            Source xmlDoc = new StreamSource("target/surefire-reports/TEST-ReportTest.xml");
            Source xslDoc = new StreamSource(createXsl());

            String outputFolder = "target/html-reports/";

            if (!new File(outputFolder).exists()) {
                new File(outputFolder).mkdirs();
            }

            String outputFileName = outputFolder + "report.html";
            OutputStream htmlFile = new FileOutputStream(outputFileName);

            Transformer transformer = factory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));

            System.out.println("OK \n#### HTML ####");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private File createXsl() {

        try {
            File xslDoc = new File("xslDoc.xsl");
            FileWriter out = null;
            out = new FileWriter(xslDoc);
            out.write("<?xml version=\"1.0\"?>\n" +
                      "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" +
                      "\n" +
                      "<xsl:template match=\"/\">\n" +
                      "<html>\n" +
                      "\n" +
                      "   <head>\n" +
                      "      <title>Test</title>\n" +
                      "      <style type=\"text/css\">\n" +
                      "        <!-- Style ici -->\n" +
                      "      </style>\n" +
                      "   </head>\n" +
                      "\n" +
                      "   <body>\n" +
                      "      <table>\n" +
                      "         <xsl:for-each select=\"testsuite\">\n" +
                      "          <li>Nombres de tests lancé : <xsl:value-of select=\"@tests\"/></li>\n" +
                      "          <li>Parmi eux <xsl:value-of select=\"@failures\"/> ont échoué..!</li>\n" +
                      "          <ul>\n" +
                      "            <xsl:for-each select=\"testcase\">\n" +
                      "              <xsl:if test=\"failure\">" +
                      "                <li>Le test : <xsl:value-of select=\"@name\"/> a echoué !</li>" +
                      "              </xsl:if>" +
                      "              <xsl:if test=\"not(failure)\">" +
                      "                <li>Le test : <xsl:value-of select=\"@name\"/> a réussi !</li>" +
                      "              </xsl:if>" +
                      "            </xsl:for-each>\n" +
                      "          </ul>\n" +
                      "         </xsl:for-each>\n" +
                      "      </table>\n" +
                      "    </body>\n" +
                      "</html>\n" +
                      "</xsl:template>\n" +
                      "</xsl:stylesheet>");

            out.close();

            return xslDoc;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
