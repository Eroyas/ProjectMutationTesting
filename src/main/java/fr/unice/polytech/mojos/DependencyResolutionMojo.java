package fr.unice.polytech.mojos;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import org.jdom.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Eroyas on 10/03/16.
 */
@Mojo(name = "dependency-resolution", defaultPhase = LifecyclePhase.VALIDATE)
public class DependencyResolutionMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    Namespace ns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        System.out.println("#### DEPENDENCY RESOLUTION MOJO ####\n...");

        Iterator<Artifact> ite = project.getDependencyArtifacts().iterator();
        Artifact art = null;

        while (ite.hasNext()) {
            art = ite.next();
            if (art.getGroupId().equals("fr.unice.polytech.mojos") && art.getArtifactId().equals("my-app")) {
                break;
            }
        }

        String pluginPath = project.getBasedir() + "/pom.xml";

        String hostPath = art.getFile().getAbsolutePath();
        hostPath = hostPath.substring(0, hostPath.lastIndexOf("/"));
        hostPath = hostPath + "/my-app-1.0-SNAPSHOT.pom";

        JDOM jdomPlugin = new JDOM(pluginPath);
        JDOM jdomHost = new JDOM(hostPath);

        List listDependencies = jdomHost.getRacine().getChildren("dependencies", ns);

        Iterator i = listDependencies.iterator();

        while(i.hasNext()) {
            Element current = (Element)i.next();

            current.addContent(jdomPlugin.getRacine().getChildren("dependency", ns));
        }

        List listRepositories = jdomHost.getRacine().getChildren("repositories", ns);

        Iterator j = listRepositories.iterator();

        while(j.hasNext()) {
            Element current = (Element)i.next();

            current.addContent(jdomPlugin.getRacine().getChildren("repository", ns));
        }

        System.out.println("OK \n#### DEPENDENCY RESOLUTION MOJO ####");
    }
}
