package fr.unice.polytech.mojos;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.net.URLClassLoader;
import java.util.List;

public class IsolatedTestRunner {

    private String path;

    public IsolatedTestRunner(String path) {
        this.path = path;
    }

    // Do not rename.
    public void runTests (List<String> testClasses, URLClassLoader cl) {

        System.out.println("#### ISOLATED TEST RUNNER ####\n...");

        // Load classes
        Class<?>[] classes = new Class<?>[testClasses.size()];

        for (int i = 0; i < testClasses.size(); i++) {

            String test = testClasses.get(i);

            try {
                classes[i] = cl.loadClass(test);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Run
        try {
            JUnitCore jUnitCore = new JUnitCore();
            jUnitCore.addListener(new JUnitRunListener(classes.length));

            Result result = jUnitCore.run(classes);

            JUnitResultProducer.getResultContent(this.path,result, classes);

            System.out.println("OK \n#### ISOLATED TEST RUNNER ####");
        } catch (Exception e) {}
    }
}





