package fr.unice.polytech.mojos;

import fr.unice.polytech.locators.Locator;
import fr.unice.polytech.mojos.util.FileUtils;
import fr.unice.polytech.mojos.wrappers.Mutator;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import spoon.processing.AbstractProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Mojo(name = "generate-mutations", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MutationMojo extends AbstractMojo {

    /**
     * permet la récupération d'information sur le project qui utilise notre plugin
     * la variable project est remplie autmatiquement
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    @Parameter(name = "processors", required = true, readonly = true)
    private List<String> processors;

    @Parameter(name = "locators", required = true, readonly = true)
    private List<String> locators;

    @Parameter(name = "packages", readonly = true)
    private List<String> packages = new ArrayList<>();

    public static int mutationsNumber;


    public void execute() throws MojoExecutionException, MojoFailureException {
        // compute number of mutation
        mutationsNumber = (locators.size() + packages.size()) * processors.size();
        int mutationsNumberCopy = mutationsNumber - 1;
        Mutator mutator;
        System.out.println("#####################  MU-TEST  #####################\n");
        // run through all mutation
        for (int i = 0; i < mutationsNumber; i++) {
            System.out.println("[MU-TEST] mutation number : " + i);
            // create mutation directory
            String destination = project.getBasedir().toString() + "/target/generated-sources/mutations/m" + i + "/src";
            new File(destination).mkdirs();
            FileUtils.cloneFolder(project.getBasedir().toString() + "/src", destination);
        }
        try {
            // init iterator
            Iterator<Artifact> it = project.getDependencyArtifacts().iterator();

            String[] spoonClassPath = new String[project.getDependencyArtifacts().size() + 1];
            Artifact a;
            int r = 0;
            // run through all artifacts
            while (it.hasNext()) {
                a = it.next();
                spoonClassPath[r] = a.getFile().getAbsolutePath();
                r++;
            }
            spoonClassPath[r] = project.getBasedir().getAbsolutePath() + "/target/classes";
            spoonClassPath[r] = project.getBasedir().getAbsolutePath() + "/target/test-classes";

            // run through all processors
            for (int i = 0; i < processors.size(); i++) {
                // run through all locators
                for (int j = 0; j < locators.size(); j++) {
                    // init mutator
                    mutator = new Mutator(project.getBasedir().toString() + "/target/generated-sources/mutations/m" + mutationsNumberCopy);
                    // mutate it
                    mutator.mutate((Locator) Class.forName("fr.unice.polytech.locators." + locators.get(j)).newInstance(), (AbstractProcessor) Class.forName("fr.unice.polytech.spoonProcesses." + processors.get(i)).newInstance());
                    mutationsNumberCopy--;
                }
                // run through all package name
                for (String aPackage : packages) {
                    // init mutator
                    mutator = new Mutator(project.getBasedir().toString() + "/target/generated-sources/mutations/m" + mutationsNumberCopy);
                    // mutate it
                    mutator.mutate((Locator) Class.forName("fr.unice.polytech.locators.PackageLocator").getConstructor(String.class).newInstance(aPackage), (AbstractProcessor) Class.forName("fr.unice.polytech.spoonProcesses." + processors.get(i)).newInstance());
                    mutationsNumberCopy--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n###################################################\n");
    }
}
