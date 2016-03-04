package fr.unice.polytech.mojos;

import org.apache.maven.project.MavenProject;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.List;

/**
 * @author tijani on 03/03/16.
 */
public class PolyTestParser {

    private SAXBuilder builder;
    private File xmlFile;


    public PolyTestParser(String path)
    {

        this.builder = new SAXBuilder();
        this.xmlFile = new File(path);
    }


    public List<String> getProcessors()
    {
        return null;
    }

    public int getNumberMutation()
    {
        return 0;
    }
}
