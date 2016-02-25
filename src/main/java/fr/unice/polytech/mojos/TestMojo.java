package fr.unice.polytech.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author tijani on 24/02/16.
 */
public class TestMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        URL[] urls = new URL[2];
        try {
            //Class c;
            //c.getMethods()[0].invoke()
            urls[0] = Paths.get(project.getBasedir() +"/target/mutants").toUri().toURL();
           // urls[0] = Paths.get(project.).toUri().toURL();
            URLClassLoader cl = new URLClassLoader(urls);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
