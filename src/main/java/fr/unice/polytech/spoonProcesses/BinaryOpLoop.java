package fr.unice.polytech.spoonProcesses;

import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtFor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

/**
 * @author tijani on 06/03/16.
 */
public class BinaryOpLoop extends BinaryOpMutation {


    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        if (!(candidate instanceof CtBinaryOperator))
        {
            return false;
        }
        else
        {
            CtBinaryOperator op = (CtBinaryOperator) candidate;
            CtFor ctFor = candidate.getParent(new TypeFilter<>(CtFor.class));
            System.out.println(ctFor);
            return ctFor != null;
        }
    }

}
