package fr.unice.polytech.mojos;

import fr.unice.polytech.spoonProcesses.BreakProcessor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;

import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

/**
 * A goal to generate code.
 *
 * @goal generate
 * @phase generate-sources
 */
public class SpoonMojo extends AbstractMojo {


    public void execute() throws MojoExecutionException, MojoFailureException {
        Launcher launcher = new Launcher();
        final String[] args = {
               "-i", "classes/" ,"-o", "target/spooned/"
        };
        launcher.setArgs(args);
        launcher.run();

        Factory factory = launcher.getFactory();
        ProcessingManager processingManager = new QueueProcessingManager(factory);
        BreakProcessor processor = new BreakProcessor();
        processingManager.addProcessor(processor);
        processingManager.process(factory.Class().getAll());

    }
}