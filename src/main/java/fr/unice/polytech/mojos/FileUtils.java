package fr.unice.polytech.mojos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author tijani on 08/03/16.
 */
public class FileUtils {

    public static void listeRepertoire(File path, List<File> allFiles) {
        //  if directory
        if (path.isDirectory()) {
            // get all files
            File[] list = path.listFiles();
            if (list != null) {
                // run through all files and subdirectories
                for (int i = 0; i < list.length; i++) {
                    listeRepertoire(list[i], allFiles);
                }
            } else {
                System.err.println("[MU-TEST] Error reading : " + path);
            }
        } else {
            // if its a file, add it to the list
            allFiles.add(path.getAbsoluteFile());
        }
    }

    public static File[] list(String path) {
        // create directory
        File f = new File(path);
        if(!f.exists())
        {
            f.mkdirs();
        }
        // return file content list
        return f.listFiles();
    }

    public static void delete(File f) throws IOException {
        // if f is a directory, clean its content
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        // remove file
        if (!f.delete())
            throw new FileNotFoundException("[MU-TEST] Failed to delete file: " + f);
    }

    public static void cloneFolder(String source, String target) {
        // create new directory
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        }
        // copy all element in the source to the target
        for (File f : new File(source).listFiles()) {
            if (f.isDirectory()) {
                String append = "/" + f.getName();
                System.out.println("[MU-TEST] Creating '" + target + append + "': "
                        + new File(target + append).mkdir());
                cloneFolder(source + append, target + append);
            }
        }
    }
}
