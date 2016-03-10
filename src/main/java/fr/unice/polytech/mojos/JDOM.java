package fr.unice.polytech.mojos;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;

/**
 * Created by Eroyas on 10/03/16.
 */
public class JDOM {

    Namespace ns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
    static org.jdom.Document document;
    static Element racine;

    public JDOM(String path) {
        try {
            SAXBuilder sxb = new SAXBuilder();
            this.document = sxb.build(new File(path));
            this.racine = this.document.getRootElement();
        } catch (Exception e) {}
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        JDOM.document = document;
    }

    public Element getRacine() {
        return racine;
    }

    public void setRacine(Element racine) {
        JDOM.racine = racine;
    }
}
