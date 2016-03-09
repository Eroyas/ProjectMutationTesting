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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

            filesComparator();

            System.out.println("OK \n#### REPORT MOJO ####");

        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void xmlFusion() {

        try {

            File[] dir = FileUtils.list("target/surefire-reports/mutants/");
            List<File> rootFiles = new ArrayList<>();
            for(File mutantFolder : dir) {
                for(File singleXml : mutantFolder.listFiles()) {
                    rootFiles.add(singleXml);

                }

            }

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

            int stillBorn = FileUtils.list("target/generated-sources/mutations/").length - FileUtils.list("target/mutants/").length;

            xmlEventWriter.add(xmlEventFactory.createStartElement("", null, "stillborn"));
            xmlEventWriter.add(xmlEventFactory.createAttribute("stillborn", String.valueOf(stillBorn)));
            xmlEventWriter.add(xmlEventFactory.createEndElement("", null, "stillborn"));

            xmlEventWriter.add(xmlEventFactory.createEndElement("", null, "root"));
            xmlEventWriter.add(xmlEventFactory.createEndDocument());

            xmlEventWriter.close();
            xmlDoc.close();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void filesComparator() throws IOException {

        File sDir = new File("src/main/java");
        File mDir = new File("target/generated-sources/mutations/src/main/java");

        File[] sFiles = sDir.listFiles();
        File[] mFiles = mDir.listFiles();

        for (File sFile : sFiles) {
            if (sFile.getAbsolutePath().endsWith(".java")) {

                for (File mFile : mFiles) {
                    if (mFile.getAbsolutePath().endsWith(".java") && sFile.getName().equalsIgnoreCase(mFile.getName())) {

                        compareFiles(new Scanner(sFile), new Scanner(mFile));
                    }
                }
            }
        }
    }

    private void compareFiles(Scanner sFile, Scanner mFile) {

        String sLine ;
        String mLine ;

        int line = 1;

        while (sFile.hasNextLine() && mFile.hasNextLine()) {
            sLine = sFile.nextLine();
            mLine = mFile.nextLine();

            if (!sLine.equals(mLine)) {
                System.out.println("Line " + line++);
                System.out.println("< " + sLine);
                System.out.println("> " + mLine + "\n");
            }
        }
    }

}
