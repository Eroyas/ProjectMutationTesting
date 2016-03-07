package fr.unice.polytech.locators;

import fr.unice.polytech.mojos.CodeMetrics;
import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 05/03/16.
 */
public class ComplexityLocator implements Locator {


    @Override
    public boolean toBeProcessed(CtClass ctClass) {
        System.out.println("################## complexity : " + CodeMetrics.calculateComplexity(ctClass));
            return CodeMetrics.calculateComplexity(ctClass) > 21;
    }
}
