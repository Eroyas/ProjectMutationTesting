package fr.unice.polytech.mojos;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author tijani on 24/02/16.
 */
@Mojo(name = "mutation-test", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES)
public class TestMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    private String qualifiedName;
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        for (int j = 0; j < MutationMojo.mutationsNumber; j++) {
            List<String> names = new ArrayList<>();
            Iterator<Artifact> it = project.getDependencyArtifacts().iterator();
            URL[] urls = new URL[project.getDependencyArtifacts().size() + 3];
            Artifact a;
            int i = 0;
            while (it.hasNext()) {
                a = it.next();
                try {
                    urls[i] = Paths.get(a.getFile().getAbsolutePath()).toUri().toURL();
                    System.out.println(urls[i]);
                    i++;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            try {

                urls[i] = Paths.get(project.getBasedir() + "/target/mutants/m"+j).toUri().toURL();
                System.out.println(urls[i]);
                urls[i + 1] = Paths.get(project.getBasedir() + "/target/test-classes/").toUri().toURL();
                System.out.println(urls[i + 1]);
                urls[i + 2] = Paths.get(System.getProperty("java.home") + "/lib/ext/").toUri().toURL();


                URLClassLoader cl = new URLClassLoader(urls);
                System.out.println(project.getSystemClasspathElements());
                qualifiedName = "";
                Files.walk(Paths.get(project.getBasedir().toString() + "/target/test-classes/")).forEach(filePath -> {
                     qualifiedName = qualifiedName + filePath.getFileName().toString() + ".";
                    if (Files.isRegularFile(filePath)) {
                        String nameWithFolder = ((qualifiedName).substring(qualifiedName.indexOf('.')+1,qualifiedName.length()-1));
                        System.out.println("################# adding to test : " + nameWithFolder.substring(0,nameWithFolder.lastIndexOf('.')));
                        names.add(nameWithFolder.substring(0,nameWithFolder.lastIndexOf('.')));
                    }
                });
                Class<?> classRunner = Class.forName("fr.unice.polytech.mojos.IsolatedTestRunner", true, cl);
                Object runner = classRunner.newInstance();
                Method runMethod = classRunner.getMethod("runTests", List.class, URLClassLoader.class);
                runMethod.invoke(runner, names, cl);

/*
            Class<?> classRunner = Class.forName("org.junit.runner.JUnitCore",true,cl);
            Method runMethod = classRunner.getMethod("main", String[].class);
            runMethod.invoke(null,new String[] { "tester.IsoTest"});
*/
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (DependencyResolutionRequiredException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
