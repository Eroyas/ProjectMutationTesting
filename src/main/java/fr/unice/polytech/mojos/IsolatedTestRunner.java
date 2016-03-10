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

        System.out.println("[MU-TEST] ##### ISOLATED TEST RUNNER #####\n");

        // Load classes
        Class<?>[] classes = new Class<?>[testClasses.size()];

        // run through all classes
        for (int i = 0; i < testClasses.size(); i++) {
            // get class name
            String test = testClasses.get(i);
            try {
                // load it via classLoader
                classes[i] = cl.loadClass(test);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Run
        try {
            // get new instance of JUnitCore
            JUnitCore jUnitCore = new JUnitCore();
            // add a listener
            jUnitCore.addListener(new JUnitRunListener(classes.length));
            // launch tests
            Result result = jUnitCore.run(classes);
            // produce result
            JUnitResultProducer.getResultContent(this.path,result, classes);

            System.out.println("[MU-TEST] \n##### ISOLATED TEST RUNNER DONE #####");
        } catch (Exception e) {}
    }
}





