package fr.unice.polytech.mojos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author tijani on 08/03/16.
 */
public class FileUtils {


    public static File[] list(String path)
    {
        File f = new File(path);
        if(!f.exists())
        {
            f.mkdirs();
        }
        return f.listFiles();
    }

    public static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

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
