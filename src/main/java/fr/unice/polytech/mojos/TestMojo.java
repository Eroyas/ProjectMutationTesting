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

    private String qualifiedName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File[] mutants = FileUtils.list(project.getBasedir()+"/target/mutants/");
        if(!Files.exists(Paths.get(project.getBasedir()+"/target/surefire-reports/mutants/")))
        {
            new File(project.getBasedir()+"/target/surefire-reports/mutants/").mkdirs();
        }
        for (File fi : mutants) {
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

                urls[i] = Paths.get(fi.getAbsolutePath()).toUri().toURL();
                System.out.println(urls[i]);
                urls[i + 1] = Paths.get(project.getBasedir() + "/target/test-classes/").toUri().toURL();
                System.out.println(urls[i + 1]);
                urls[i + 2] = Paths.get(System.getProperty("java.home") + "/lib/ext/").toUri().toURL();


                URLClassLoader cl = new URLClassLoader(urls);
                System.out.println(project.getSystemClasspathElements());
                qualifiedName = "";
/*
                Files.walk(Paths.get(project.getBasedir().toString() + "/target/test-classes/")).forEach(filePath -> {
                    if(filePath.getFileName().toString().endsWith(".class")) {
                        qualifiedName = qualifiedName +filePath.getFileName().toString();
                    }
                    else
                    {
                        qualifiedName = qualifiedName + filePath.getFileName().toString() + ".";

                    }
                    if (Files.isRegularFile(filePath)) {
                        String nameWithFolder = ((qualifiedName).substring(0,qualifiedName.lastIndexOf('.')));
                        System.out.println("################# adding to test : " + nameWithFolder);
                        names.add(nameWithFolder);
                        qualifiedName = "";
                    }
                });
*/
                listClasses(project.getBasedir()+"/target/test-classes",names);
                System.out.println(names);

                System.out.println("################ test : "+names);
                Class<?> classRunner = Class.forName("fr.unice.polytech.mojos.IsolatedTestRunner", true, cl);
                Object runner = classRunner.getConstructor(String.class).newInstance(project.getBasedir() + "/target/surefire-reports/mutants/" + fi.getName());
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

    private void listClasses(String path,List<String> files)
    {
        //System.out.println("###################### !!!!!!! paths !!!!!! : "+path);
        File base = new File(path);
        File[] tab = base.listFiles();
        String concat;
        for(File f : tab)
        {
            concat = path +"/"+f.getName();
            if(concat.endsWith(".class"))
            {
                String toSearch = "target/test-classes/";
                String s = concat.substring(concat.indexOf(toSearch)+toSearch.length(),concat.lastIndexOf(".class")).replace('/','.');

                files.add(s);
                return;
            }
            if(Files.isDirectory(Paths.get(concat)))
            {
                listClasses(concat,files);
            }
        }

    }
}
