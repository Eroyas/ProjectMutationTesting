package fr.unice.polytech.mojos;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.net.URL;
import java.net.URLClassLoader;


public class IsolatedTestRunner {




    // Do not rename.
    public void runTests (String[] testClasses, URLClassLoader cl) {
        // Load classes
        Class<?>[] classes = new Class<?>[testClasses.length];
        System.out.println("init tab");
        for (int i=0; i<testClasses.length; i++) {
            String test =  testClasses[i];
            try {
                System.out.println("conversion to class");
                classes[i] = cl.loadClass(test);
                System.out.println(i);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Run
        System.out.println(classes[0].toString());
        System.out.println("junit core");
        try {
            Result r = JUnitCore.runClasses(classes[0]);
            System.out.println("resuuuuut");
            System.out.println(r.getFailures().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}





