package fr.unice.polytech.mojos;

import fr.unice.polytech.locators.Locator;
import fr.unice.polytech.spoonProcesses.ScopeFeildMutation;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.declaration.CtAnnotationImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author tijani on 05/03/16.
 */
public class Mutator {

    private String path;

    public Mutator(String path) {
        this.path = path;
    }

    public void mutate(Locator locator,AbstractProcessor processor) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");
        launcher.setSourceOutputDirectory(path);
        launcher.run();
        //launcher.buildModel();
        System.out.println("tesstttt");
        Factory factory = launcher.getFactory();
        List<CtClass> ctClasses = factory.Package().getRootPackage().getElements(new TypeFilter<>(CtClass.class));
        System.out.println("tesstttt");

        for (CtClass origClass : ctClasses) {
            if(locator.toBeProcessed(origClass)) {
                System.out.println("######### processed ##########");
                List<CtElement> elementsToBeMutated = origClass.getElements(processor::isToBeProcessed);
                for (CtElement e : elementsToBeMutated) {

                    // this loop is the trickiest part
                    // because we want one mutation after the other

                    // cloning the AST element
                    System.out.println("cloning");
                    CtElement op = launcher.getFactory().Core().clone(e);

                    // mutate the element
                    System.out.println("mutate");

                    processor.process(op);

                    // temporarily replacing the original AST node with the mutated element
                    System.out.println("replace");
                    e.replace(op);
                    System.out.println("create class");
                    // creating a new class containing the mutating code
                    CtClass klass = launcher.getFactory().Core()
                            .clone(op.getParent(CtClass.class));
                    // setting the package
                    System.out.println("set package");
                    klass.setParent(origClass.getParent());
                    System.out.println(klass);


                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(path+klass.getQualifiedName().replace('.','/')+".java"), "utf-8"))) {
                        writer.write("package " + klass.getPackage() + ";\n" + klass.toString());
                    } catch (IOException d) {
                        d.printStackTrace();
                    }
                    // adding the new mutant to the list
                    // restoring the original code
                    op.replace(e);
                }

            }
        }
    }
}
