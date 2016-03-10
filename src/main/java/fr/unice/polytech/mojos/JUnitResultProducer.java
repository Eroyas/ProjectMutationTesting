package fr.unice.polytech.mojos;

import org.junit.runner.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Eroyas on 09/03/16.
 */
public class JUnitResultProducer {

    public static void getResultContent(String path, Result result, Class<?>[] classes) {

        // create directory
        if(!Files.exists(Paths.get(path))) {
            new File(path).mkdirs();
        }

        // init variables
        int numberOfTest = result.getRunCount();
        int numberOfTestFail = result.getFailureCount();
        int numberOfTestIgnore = result.getIgnoreCount();

        // run through classes
        for (int i = 0; i < classes.length; i++) {
            // init name
            String reportFileName = "JUnitCore-Report" + classes[i].getName() + ".xml";
            // init buffer
            StringBuffer myContent = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<testsuite ");
            // append result
            myContent.append("tests=\"" + numberOfTest + "\" ");
            myContent.append("failures=\"" + numberOfTestFail + "\" ");
            myContent.append("name=\"" + classes[i].getName() + "\" ");
            myContent.append("skipped=\"" + numberOfTestIgnore + "\">\n");

            // get all method of the class
            Method[] methods = classes[i].getDeclaredMethods();

            // run through all methods
            for (int j = 0; j < methods.length; j++) {
                // append result
                myContent.append("<testcase classname=\"" + classes[i].getName() + "\" ");
                myContent.append("name=\"" + methods[j].getName() + "\">\n");
                // run through all failures
                for (int k = 0; k < result.getFailures().size(); k++) {
                    if (methods[j].getName().equalsIgnoreCase(result.getFailures().get(k).getDescription().getMethodName())) {
                        myContent.append("<failure type=\"" + result.getFailures().get(k).getException() + "\">");
                        myContent.append("</failure>\n");
                    }
                }
                // close tag
                myContent.append("</testcase>\n");
            }

            // close tag
            myContent.append("</testsuite>\n");

            writeReportFile(path + "/" + reportFileName, myContent);
        }
    }

    private static void writeReportFile(String fileName, StringBuffer reportContent) {
        FileWriter myFileWriter = null;

        try {
            // write content into the file
            myFileWriter = new FileWriter(fileName);
            myFileWriter.write(reportContent.toString());
        } catch (IOException e) {

        } finally {
            if (myFileWriter!=null) {
                try {
                    // close writer
                    myFileWriter.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
