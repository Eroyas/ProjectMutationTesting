package fr.unice.polytech.mojos;

import fr.unice.polytech.locators.Locator;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * @author tijani on 05/03/16.
 */
public class Mutator {

    private String path;

    public Mutator(String path) {
        this.path = path;
    }

    public void mutate(Locator locator,AbstractProcessor processor,String[] spoonClassPath) {
        int processingDector = 0;
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");
        launcher.setSourceOutputDirectory(path + "/src/main/java");
        launcher.getModelBuilder().setSourceClasspath(spoonClassPath);
        launcher.run();
        //launcher.buildModel();
        Factory factory = launcher.getFactory();

        List<CtClass> ctClasses = factory.Package().getRootPackage().getElements(new TypeFilter<>(CtClass.class));
        System.out.println("######### mutant : " + path);
        for (CtClass origClass : ctClasses) {
            if(locator.toBeProcessed(origClass)) {
                System.out.println("######### processed ##########");
                List<CtElement> elementsToBeMutated = origClass.getElements(processor::isToBeProcessed);
                for (CtElement e : elementsToBeMutated) {
                    processingDector++;
                    // this loop is the trickiest part
                    // because we want one mutation after the other

                    // cloning the AST element
                    CtElement op = launcher.getFactory().Core().clone(e);

                    // mutate the element

                    processor.process(op);

                    // temporarily replacing the original AST node with the mutated element
                    e.replace(op);
                    // creating a new class containing the mutating code
                    CtClass klass = launcher.getFactory().Core()
                            .clone(op.getParent(CtClass.class));
                    // setting the package
                    klass.setParent(origClass.getParent());


                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(path+"/src/main/java/"+klass.getQualifiedName().replace('.','/')+".java"), "utf-8"))) {
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

        if(processingDector == 0)
        {
            try {
                FileUtils.delete(new File(path));
                MutationMojo.mutationsNumber--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
