package fr.unice.polytech.mojos;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import java.util.List;
import java.util.Iterator;

/**
 * Created by Eroyas on 10/03/16.
 */
public class JDOM {

    static org.jdom.Document document;
    static Element racine;

    public JDOM(String path) {
        try {
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(new File(path));
            racine = document.getRootElement();
        } catch (Exception e) {}
    }

    static void affiche() {
        List listElement = racine.getChildren("modelVersion"); // ici on essaye de récup..
        List listElement2 = racine.getContent();

        System.out.println(racine.getName()); // OK !! RACINE = project
        System.out.println(listElement); // MAIS ICI vide..
        System.out.println(listElement2);

        Iterator i = listElement.iterator();
        while(i.hasNext()) {
            Element courant = (Element)i.next();
            System.out.println(courant.getParent());
        }
    }
}
