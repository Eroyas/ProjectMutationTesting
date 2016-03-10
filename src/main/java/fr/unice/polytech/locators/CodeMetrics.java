package fr.unice.polytech.locators;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.Iterator;

/**
 * @author tijani on 05/03/16.
 */
public class CodeMetrics {


    public static double calculateComplexity(CtClass cl)
    {
        if(cl.getMethods().size() == 0)
        {
            return 1;
        }
        Iterator<CtMethod> it = cl.getMethods().iterator();
        CtMethod meth;
        double complexity = 0;

        // run through all methods
        while(it.hasNext())
        {
            meth = it.next();
            // compute complexity depending on number of if, switch and for/while
            complexity = complexity +
                    (meth.getBody().getElements(new TypeFilter<>(CtIf.class)).size() +
                    meth.getBody().getElements(new TypeFilter<>(CtSwitch.class)).size() +
                     meth.getBody().getElements(new TypeFilter<>(CtLoop.class)).size()) + 1;
        }
        return complexity / cl.getMethods().size();
    }

    public static int linesOfCode(CtClass cl)
    {
        if(cl.getMethods().size() == 0)
        {
            return 1;
        }
        Iterator<CtMethod> it = cl.getMethods().iterator();
        CtMethod meth;
        int lines = 0;

        // run through all methods
        while(it.hasNext())
        {
            meth = it.next();
            // compute line numbers
            lines = lines + meth.getBody().getStatements().size();
        }
        return (lines / cl.getMethods().size());

    }
}
