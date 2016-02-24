package fr.unice.polytech.mojos;

import fr.unice.polytech.spoonProcesses.BinaryOpMutation;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import spoon.Launcher;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import java.io.*;
import java.util.List;


@Mojo( name = "generate-mutations", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MutationMojo extends AbstractMojo {

    /**
     * permet la récupération d'information sur le project qui utilise notre plugin
     * la variable project est remplie autmatiquement
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = false)
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("#####################  my_plugin  #####################################\n");

        String destination = project.getBasedir().toString()+"/target/generated-sources/mutations/src";
        new File(destination).mkdir();
        cloneFolder(project.getBasedir().toString()+"/src",destination);
        System.out.println("\n#######################################################################\n");
        //JavaCompiler c  = ToolProvider.getSystemJavaCompiler();

        getLog().info("totototo");
        Launcher launcher = new Launcher();
        /*
        final String[] args = {
                "-i" ,"src/main/java" ,"-o", "target/generated-sources/mutations/src/main/java"
        };
        launcher.setArgs(args);*/
        launcher.addInputResource("src/main/java");
        launcher.setSourceOutputDirectory("target/generated-sources/mutations/src/main/java");
        launcher.run();
        //launcher.buildModel();

        BinaryOpMutation mutator = new BinaryOpMutation();
        Factory factory = launcher.getFactory();
        CtClass origClass = (CtClass) factory.Package().getRootPackage()
                .getElements(new TypeFilter(CtClass.class)).get(0);

        // now we apply a transformation
        // we replace "+" and "*" by "-"
        List<CtElement> elementsToBeMutated = origClass.getElements(mutator::isToBeProcessed);

        for (CtElement e : elementsToBeMutated) {
            // this loop is the trickiest part
            // because we want one mutation after the other

            // cloning the AST element
            CtElement op = launcher.getFactory().Core().clone(e);

            // mutate the element
            mutator.process(op);

            // temporarily replacing the original AST node with the mutated element
            replace(e,op);

            // creating a new class containing the mutating code
            CtClass klass = launcher.getFactory().Core()
                    .clone(op.getParent(CtClass.class));
            // setting the package
            klass.setParent(origClass.getParent());
            System.out.println(klass);


            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(project.getBasedir().toString()+"/target/generated-sources/mutations/src/main/java/"+klass.getQualifiedName().replace('.','/')+".java"), "utf-8"))) {
                writer.write(klass.toString());
            }
            catch (IOException d)
            {
                d.printStackTrace();
            }
            // adding the new mutant to the list
            // restoring the original code
            replace(op, e);
        }
    }


    private void replace(CtElement e, CtElement op) {
        if (e instanceof CtStatement  && op instanceof CtStatement) {
            ((CtStatement)e).replace((CtStatement) op);
            return;
        }
        if (e instanceof CtExpression && op instanceof CtExpression) {
            ((CtExpression)e).replace((CtExpression) op);
            return;
        }
        throw new IllegalArgumentException(e.getClass()+" "+op.getClass());
    }

    // System.out.println(project.getTestCompileSourceRoots());
    //   System.out.println(project.getCompileSourceRoots().remove(0));
    ///home/user/Desktop/si4/semestre2/DevOps/mutation/DevObs_10/my-app/src/main/java
    //project.addCompileSourceRoot(project.getBasedir().toString()+"/target/mutations/main/java/com/mycompany/app");

    /**
     System.out.println("avant   "+project.getBuild().getSourceDirectory());

     project.getBuild().setSourceDirectory(project.getBasedir().toString() +
     "/target/mutations/main/java");
     System.out.println("apres  "+project.getBuild().getSourceDirectory());
     **/

    /**
     * merci stackoverflow :)
     * @param source
     * @param target
     */
    public static void cloneFolder(String source, String target) {
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        }
        for (File f : new File(source).listFiles()) {
            if (f.isDirectory()) {
                String append = "/" + f.getName();
                System.out.println("Creating '" + target + append + "': "
                        + new File(target + append).mkdir());
                cloneFolder(source + append, target + append);
            }
        }
    }
}
