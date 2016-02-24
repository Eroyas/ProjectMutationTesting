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
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> optionList = new ArrayList<>();

      //  JavaFileManager jfm = compiler.getStandardFileManager(null,null,null);
       // Iterable<? extends JavaFileObject> compUnits =
        StandardJavaFileManager fm = compiler.getStandardFileManager(null,null,null);
        Iterable<? extends JavaFileObject> compUnits = fm.getJavaFileObjects(new File(project.getBasedir().toString()+"/src/main/java/tester/Iso"));
// set compiler's classpath to be same as the runtime's
        optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));
        optionList.addAll(Arrays.asList("-d",project.getBasedir().toString()+"/target/generated-sources/mutations/src"));


        JavaCompiler.CompilationTask task = compiler.getTask(null,fm,null,optionList,null,compUnits);
        task.call();

    }
}
