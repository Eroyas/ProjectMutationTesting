package fr.unice.polytech.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author tijani on 23/02/16.
 */
@Mojo(name = "consider",defaultPhase = LifecyclePhase.GENERATE_RESOURCES)

public class ConsiderMutationMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println(project.getCompileSourceRoots());
        project.getCompileSourceRoots().remove(0);
        project.addCompileSourceRoot(project.getBasedir().toString() + "/target/generated-sources/mutations/src/main/java");

    }
}
