package fr.unice.polytech.mojos;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Eroyas on 09/03/16.
 */
public class Comparator {

    public static void filesComparator(String pathSource) throws IOException {

        List<File> mRootFiles = new ArrayList<>();
        FileUtils.listeRepertoire(new File("target/generated-sources/mutations/"), mRootFiles);

        List<File> sRootFiles = new ArrayList<>();
        FileUtils.listeRepertoire(new File(pathSource), sRootFiles);

        for (File sFile : sRootFiles) {
            if (sFile.getAbsolutePath().endsWith(".java")) {

                for (File mFile : mRootFiles) {
                    if (mFile.getAbsolutePath().endsWith(".java") && sFile.getName().equalsIgnoreCase(mFile.getName())) {

                        compareFiles(new Scanner(sFile), new Scanner(mFile));
                    }
                }
            }
        }
    }

    private static void compareFiles(Scanner sFile, Scanner mFile) {

        String sLine ;
        String mLine ;

        int line = 1;

        while (sFile.hasNextLine() && mFile.hasNextLine()) {
            sLine = sFile.nextLine();
            mLine = mFile.nextLine();

            if (!sLine.equals(mLine)) {
                System.out.println("Line " + line++);
                System.out.println("< " + sLine);
                System.out.println("> " + mLine + "\n");
            }
        }
    }
}
