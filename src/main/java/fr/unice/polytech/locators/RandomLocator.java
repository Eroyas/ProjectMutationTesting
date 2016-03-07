package fr.unice.polytech.locators;

import spoon.reflect.declaration.CtClass;

import java.util.Random;

/**
 * @author tijani on 05/03/16.
 */
public class RandomLocator implements Locator{
    @Override
    public boolean toBeProcessed(CtClass ctClass) {
        return new Random().nextDouble() > 0.5;
    }
}
