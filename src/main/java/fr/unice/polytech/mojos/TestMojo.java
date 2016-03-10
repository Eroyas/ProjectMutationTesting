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

import java.io.File;
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


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // get all mutants
        File[] mutants = FileUtils.list(project.getBasedir() + "/target/mutants/");

        if (!Files.exists(Paths.get(project.getBasedir() + "/target/surefire-reports/mutants/"))) {
            new File(project.getBasedir() + "/target/surefire-reports/mutants/").mkdirs();
        }

        // run through all mutants
        for (File fi : mutants) {
            // init nales
            List<String> names = new ArrayList<>();
            Iterator<Artifact> it = project.getDependencyArtifacts().iterator();
            URL[] urls = new URL[project.getDependencyArtifacts().size() + 3];
            Artifact a;
            int i = 0;
            // run through all artifacts
            while (it.hasNext()) {
                a = it.next();
                try {
                    // get file paths
                    urls[i] = Paths.get(a.getFile().getAbsolutePath()).toUri().toURL();
                    i++;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            try {

                urls[i] = Paths.get(fi.getAbsolutePath()).toUri().toURL();
                urls[i + 1] = Paths.get(project.getBasedir() + "/target/test-classes/").toUri().toURL();
                urls[i + 2] = Paths.get(System.getProperty("java.home") + "/lib/ext/").toUri().toURL();

                URLClassLoader cl = new URLClassLoader(urls);

                listClasses(project.getBasedir() + "/target/test-classes", names);

                System.out.println("[MU-TEST] Testing : " + names);
                // get class
                Class<?> classRunner = Class.forName("fr.unice.polytech.mojos.IsolatedTestRunner", true, cl);
                // invoke constructor
                Object runner = classRunner.getConstructor(String.class).newInstance(project.getBasedir() + "/target/surefire-reports/mutants/" + fi.getName());
                // get method runTests
                Method runMethod = classRunner.getMethod("runTests", List.class, URLClassLoader.class);
                // invoke it
                runMethod.invoke(runner, names, cl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void listClasses(String path, List<String> files) {
        File base = new File(path);
        File[] tab = base.listFiles();
        String concat;
        // run through all content file
        for (File f : tab) {
            concat = path + "/" + f.getName();
            // put name into list if its a class file
            if (concat.endsWith(".class")) {
                String toSearch = "target/test-classes/";
                String s = concat.substring(concat.indexOf(toSearch) + toSearch.length(), concat.lastIndexOf(".class")).replace('/', '.');
                files.add(s);
                return;
            }
            // otherwise recall method on the dir
            if (Files.isDirectory(Paths.get(concat))) {
                listClasses(concat, files);
            }
        }
    }
}
