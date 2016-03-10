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

        if(!Files.exists(Paths.get(path))) {
            new File(path).mkdirs();
        }

        int numberOfTest = result.getRunCount();
        int numberOfTestFail = result.getFailureCount();
        int numberOfTestIgnore = result.getIgnoreCount();

        for (int i = 0; i < classes.length; i++) {

            String reportFileName = "JUnitCore-Report" + classes[i].getName() + ".xml";

            StringBuffer myContent = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<testsuite ");

            myContent.append("tests=\"" + numberOfTest + "\" ");
            myContent.append("failures=\"" + numberOfTestFail + "\" ");
            myContent.append("name=\"" + classes[i].getName() + "\" ");
            myContent.append("skipped=\"" + numberOfTestIgnore + "\">\n");

            Method[] methods = classes[i].getDeclaredMethods();

            for (int j = 0; j < methods.length; j++) {

                myContent.append("<testcase classname=\"" + classes[i].getName() + "\" ");
                myContent.append("name=\"" + methods[j].getName() + "\">\n");

                for (int k = 0; k < result.getFailures().size(); k++) {
                    System.out.println("################ meth de test :" + methods[j].getName() + "############## failure :" +result.getFailures().get(k).getDescription().getMethodName() );
                    if (methods[j].getName().equalsIgnoreCase(result.getFailures().get(k).getDescription().getMethodName())) {
                        myContent.append("<failure type=\"" + result.getFailures().get(k).getException() + "\">");
                        myContent.append("</failure>\n");
                    }
                }

                myContent.append("</testcase>\n");
            }

            myContent.append("</testsuite>\n");

            writeReportFile(path + "/" + reportFileName, myContent);
        }
    }

    private static void writeReportFile(String fileName, StringBuffer reportContent) {
        FileWriter myFileWriter = null;

        try {
            myFileWriter = new FileWriter(fileName);
            myFileWriter.write(reportContent.toString());
        } catch (IOException e) {

        } finally {
            if (myFileWriter!=null) {
                try {
                    myFileWriter.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
