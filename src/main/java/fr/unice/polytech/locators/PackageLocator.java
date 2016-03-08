package fr.unice.polytech.locators;

import spoon.reflect.declaration.CtClass;

/**
 * @author tijani on 05/03/16.
 */
public class PackageLocator implements Locator {

    private String packageName;


    public PackageLocator(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean toBeProcessed(CtClass ctClass) {
        String[] name = ctClass.getQualifiedName().split("\\.");
        String[] packageSplit = packageName.split("\\.");
        if(name.length < 2)
        {
            name = new String[2];
            name[0] = "";
            name[1] = ctClass.getSimpleName();
        }
        if(packageSplit.length == 0)
        {
            packageSplit = new String[1];
            packageSplit[0] = packageName;
        }
        return name[name.length-2].equals(packageSplit[packageSplit.length-1]);
    }
}
