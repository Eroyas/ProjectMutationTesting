package fr.unice.polytech.mojos;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
        File f = new File(project.getBasedir().toString()+"/target/generated-sources/mutations");
        if(!f.exists())
        {
            f.mkdirs();
        }
        File[] tab = f.listFiles();


        for (File fi : tab) {
            String outputDirectory =  project.getBasedir().toString()+"/target/mutants/"+(fi.getAbsolutePath().substring(fi.getAbsolutePath().lastIndexOf("/"),fi.getAbsolutePath().length()));
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            List<String> optionList = new ArrayList<>();
            DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fm = compiler.getStandardFileManager(diagnosticsCollector, null, null);
            List<String> names = new ArrayList<>();
            try {

                Files.walk(Paths.get(fi.getAbsolutePath())).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        System.out.println("compiling : " + filePath.toString());
                        names.add(filePath.toString());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterable<? extends JavaFileObject> compUnits = fm.getJavaFileObjectsFromStrings(names);
// set compiler's classpath to be same as the runtime's
            optionList.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));
            File targetFile = new File(outputDirectory);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            optionList.addAll(Arrays.asList("-d",outputDirectory));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnosticsCollector, optionList, null, compUnits);
            task.call();
            if(diagnosticsCollector.getDiagnostics().size() != 0)
            {
                System.out.println("########## mutant : " + fi.getName() + " is stillborn");
                fi.delete();
            }

        }
    }
}
