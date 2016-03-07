package fr.unice.polytech.locators;

import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 05/03/16.
 */
public interface Locator
{

    boolean toBeProcessed(CtClass ctClass);
}
