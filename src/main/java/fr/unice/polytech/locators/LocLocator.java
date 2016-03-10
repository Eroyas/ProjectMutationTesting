package fr.unice.polytech.locators;

import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 08/03/16.
 */
public class LocLocator implements Locator {



    @Override
    public boolean toBeProcessed(CtClass ctClass) {
        return CodeMetrics.linesOfCode(ctClass) > 35;
    }
}
