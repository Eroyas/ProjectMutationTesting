package fr.unice.polytech.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

/**
 * @author tijani on 03/03/16.
 */
//@Mojo(name = "loop-test",defaultPhase = LifecyclePhase.PACKAGE)
public class LoopTestMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;




    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        while (TestMojo.mutationsNumber != 0) {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(project.getBasedir()+"/pom.xml"));
            request.setGoals(Collections.singletonList("test"));
            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getProperty("maven.home")));
            System.out.println(TestMojo.mutationsNumber);
            try {
                invoker.execute(request);
            } catch (MavenInvocationException e) {
                e.printStackTrace();
            }
        }
    }
}
