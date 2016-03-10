package fr.unice.polytech.spoonProcesses;

import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

/**
 * @author tijani on 06/03/16.
 */
public class BinaryOpLoop extends BinaryOpMutation {


    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        // if the candidate doesn't match, return
        if (!(candidate instanceof CtBinaryOperator))
        {
            return false;
        }
        else
        {
            // check if the candidate's parent is a loop
            CtLoop ctFor = candidate.getParent(new TypeFilter<>(CtLoop.class));
            return ctFor != null;
        }
    }

}
