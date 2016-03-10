package fr.unice.polytech.mojos;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author tijani on 24/02/16.
 */
@Mojo(name ="compile-mutants", defaultPhase = LifecyclePhase.COMPILE)
public class CompileMutantsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // get file
        File f = new File(project.getBasedir().toString()+"/target/generated-sources/mutations");
        // create directory if not existing
        if(!f.exists())
        {
            f.mkdirs();
        }
        // list content
        File[] tab = f.listFiles();

        // run through all files
        for (File fi : tab) {
            // output directory
            String outputDirectory =  project.getBasedir().toString()+"/target/mutants/"+(fi.getAbsolutePath().substring(fi.getAbsolutePath().lastIndexOf("/"),fi.getAbsolutePath().length()));

            // get java compiler
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            // init list option
            List<String> optionList = new ArrayList<>();

            // init diagnostic collector
            DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();

            // get standard FM
            StandardJavaFileManager fm = compiler.getStandardFileManager(diagnosticsCollector, null, null);

            // init names list
            List<String> names = new ArrayList<>();
            try {
                // for all files
                Files.walk(Paths.get(fi.getAbsolutePath())).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        System.out.println("[MU-TEST] Compiling : " + filePath.toString());
                        // add the name to the lsit
                        names.add(filePath.toString());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            // get file objects
            Iterable<? extends JavaFileObject> compUnits = fm.getJavaFileObjectsFromStrings(names);
            // init iterator
            Iterator<Artifact> it = project.getDependencyArtifacts().iterator();
            // init dependencies
            List<String> dependencies = new ArrayList<>();
            dependencies.add("-classpath");

            Artifact a;
            String cp = System.getProperty("java.class.path");
            // run through all artifact
            while (it.hasNext()) {
                a = it.next();
                cp = cp +":"+a.getFile().getAbsolutePath();
            }
            dependencies.add(cp);
            System.out.println("[MU-TEST] Dependency found : " + cp);
            optionList.addAll(dependencies);
            // create target dir
            File targetFile = new File(outputDirectory);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            optionList.addAll(Arrays.asList("-d",outputDirectory));
            // call compiler
            JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnosticsCollector, optionList, null, compUnits);
            task.call();
            // if erros
            if(diagnosticsCollector.getDiagnostics().size() != 0)
            {
                System.out.println("[MU-TEST] Mutant : " + fi.getAbsolutePath() + " is still born !");
                try {
                    Files.delete(Paths.get(project.getBasedir()+"/target/mutants/"+fi.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
