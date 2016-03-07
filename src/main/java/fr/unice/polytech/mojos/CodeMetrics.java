package fr.unice.polytech.mojos;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.Iterator;

/**
 * @author tijani on 05/03/16.
 */
public class CodeMetrics {


    public static double calculateComplexity(CtClass cl)
    {
        Iterator<CtMethod> it = cl.getMethods().iterator();
        CtMethod meth;
        double complexity = 0;
        while(it.hasNext())
        {
            meth = it.next();
            complexity = complexity +
                    (meth.getBody().getElements(new TypeFilter<>(CtIf.class)).size() +
                    meth.getBody().getElements(new TypeFilter<>(CtSwitch.class)).size() +
                     meth.getBody().getElements(new TypeFilter<>(CtLoop.class)).size()) + 1;
        }
        return complexity / cl.getMethods().size();
    }

    public static int linesOfCode(CtMethod meth)
    {
        return 0;
    }
}
