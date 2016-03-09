package fr.unice.polytech.mojos;

import fr.unice.polytech.locators.Locator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import spoon.processing.AbstractProcessor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Mojo( name = "generate-mutations", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MutationMojo extends AbstractMojo {

    /**
     * permet la récupération d'information sur le project qui utilise notre plugin
     * la variable project est remplie autmatiquement
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    @Parameter(name = "processors",required = true, readonly = true)
    private List<String> processors;

    @Parameter(name = "locators", required = true, readonly = true)
    private List<String> locators;

    @Parameter(name = "packages",readonly = true)
    private List<String> packages = new ArrayList<>();

    public static int mutationsNumber;



    public void execute() throws MojoExecutionException, MojoFailureException {
        mutationsNumber = (locators.size()+ packages.size()) * processors.size();
        int mutationsNumberCopy = mutationsNumber-1;
        Mutator mutator;
        System.out.println("#####################  my_plugin  #####################################\n");
        for(int i=0; i < mutationsNumber;i++)
        {
            System.out.println(i);
            String destination = project.getBasedir().toString()+"/target/generated-sources/mutations/m"+i+"/src";
            new File(destination).mkdirs();
            FileUtils.cloneFolder(project.getBasedir().toString() + "/src", destination);
        }
        try {
        for (int i=0; i < processors.size();i++)
        {
            for (int j=0; j < locators.size();j++)
            {
                mutator = new Mutator( project.getBasedir().toString()+"/target/generated-sources/mutations/m"+ mutationsNumberCopy);

                    mutator.mutate((Locator)Class.forName("fr.unice.polytech.locators."+locators.get(j)).newInstance(),(AbstractProcessor)Class.forName("fr.unice.polytech.spoonProcesses."+processors.get(i)).newInstance());
                mutationsNumberCopy --;
            }

            for (String aPackage : packages) {
                mutator = new Mutator(project.getBasedir().toString() + "/target/generated-sources/mutations/m" + mutationsNumberCopy);
                mutator.mutate((Locator) Class.forName("fr.unice.polytech.locators.PackageLocator").getConstructor(String.class).newInstance(aPackage), (AbstractProcessor) Class.forName("fr.unice.polytech.spoonProcesses." + processors.get(i)).newInstance());
                mutationsNumberCopy--;
            }
        }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("\n#######################################################################\n");

    }


    /**
     * merci stackoverflow :)
     * @param source
     * @param target
     */

}
