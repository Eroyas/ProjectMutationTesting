package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

/**
 * @author tijani on 11/02/16.
 */
public class BinaryOpMutation extends AbstractProcessor<CtElement> {

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtBinaryOperator;
    }

    @Override
    public void process(CtElement candidate) {
        // if the candidate doesn't match, return
        if (!(candidate instanceof CtBinaryOperator)) {
            return;
        }

        // replace the binary opertor by a /
        CtBinaryOperator op = (CtBinaryOperator)candidate;
        op.setKind(BinaryOperatorKind.DIV);
    }
}
