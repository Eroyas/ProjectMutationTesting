package fr.unice.polytech.locators;

import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 05/03/16.
 */
public class ComplexityLocator implements Locator {


    @Override
    public boolean toBeProcessed(CtClass ctClass) {
            return CodeMetrics.calculateComplexity(ctClass) > 21;
    }
}
