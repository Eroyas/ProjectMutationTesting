package fr.unice.polytech.locators;

import fr.unice.polytech.mojos.CodeMetrics;
import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 08/03/16.
 */
public class LocLocator implements Locator {

    private int loc;

    public LocLocator(int loc) {
        this.loc = loc;
    }

    @Override
    public boolean toBeProcessed(CtClass ctClass) {
        return CodeMetrics.linesOfCode(ctClass) > loc;
    }
}
