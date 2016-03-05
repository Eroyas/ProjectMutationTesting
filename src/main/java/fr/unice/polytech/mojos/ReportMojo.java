package fr.unice.polytech.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.*;
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
            System.out.println("#### REPORT MOJO ####\n...");
            TransformerFactory factory = TransformerFactory.newInstance();

            xmlFusion();
            Source xmlDoc = new StreamSource("target/surefire-reports/TestsReport.xml");
            Source xslDoc = new StreamSource(ReportMojo.class.getClassLoader().getResourceAsStream("xslDoc.xsl"));

            String outputFolder = "target/html-reports/";

            if (!new File(outputFolder).exists()) {
                new File(outputFolder).mkdirs();
            }

            String outputFileName = outputFolder + "report.html";
            OutputStream htmlFile = new FileOutputStream(outputFileName);

            Transformer transformer = factory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));

            System.out.println("OK \n#### REPORT MOJO ####");

        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void xmlFusion() {

        try {
            File oldReport = new File("target/surefire-reports/TestsReport.xml");
            oldReport.delete();

            File dir = new File("target/surefire-reports");
            File[] rootFiles = dir.listFiles();

            Writer xmlDoc = null;
            xmlDoc = new FileWriter("target/surefire-reports/TestsReport.xml");

            XMLOutputFactory xmlOutFactory = XMLOutputFactory.newFactory();
            XMLEventWriter xmlEventWriter = xmlOutFactory.createXMLEventWriter(xmlDoc);
            XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();

            xmlEventWriter.add(xmlEventFactory.createStartDocument());
            xmlEventWriter.add(xmlEventFactory.createStartElement("", null, "root"));

            XMLInputFactory xmlInFactory = XMLInputFactory.newFactory();

            for (File rootFile : rootFiles) {
                if (rootFile.getAbsolutePath().endsWith(".xml")) {
                    XMLEventReader xmlEventReader = xmlInFactory.createXMLEventReader(new StreamSource(rootFile));
                    XMLEvent event = xmlEventReader.nextEvent();

                    while (event.getEventType() != XMLEvent.START_ELEMENT) {
                        event = xmlEventReader.nextEvent();
                    }

                    do {
                        xmlEventWriter.add(event);
                        event = xmlEventReader.nextEvent();
                    } while (event.getEventType() != XMLEvent.END_DOCUMENT);

                    xmlEventReader.close();
                }
            }

            xmlEventWriter.add(xmlEventFactory.createEndElement("", null, "root"));
            xmlEventWriter.add(xmlEventFactory.createEndDocument());

            xmlEventWriter.close();
            xmlDoc.close();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

}
