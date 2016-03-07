package fr.unice.polytech.mojos;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;


public class IsolatedTestRunner {




    // Do not rename.
    public void runTests (List<String> testClasses, URLClassLoader cl) {
        // Load classes
        Class<?>[] classes = new Class<?>[testClasses.size()];
        System.out.println("init tab");
        for (int i=0; i<testClasses.size(); i++) {
            String test =  testClasses.get(i);
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
            Result r = JUnitCore.runClasses(classes);
            System.out.println("resuuuuut");
            System.out.println("########## nb failures : " + r.getFailures().size());
            if(r.getFailures().size() != 0)
            System.out.println(r.getFailures().get(0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}





