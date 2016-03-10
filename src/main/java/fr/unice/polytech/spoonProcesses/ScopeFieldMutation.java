package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;

/**
 * @author tijani on 05/03/16.
 */
public class ScopeFieldMutation extends AbstractProcessor<CtElement> {

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtField;
    }

    @Override
    public void process(CtElement ctField) {
        // if the candidate doesn't match, return
        if (!(ctField instanceof CtField)) {
            return;
        }

        // set visibility to "final"
        CtField op = (CtField)ctField;
        op.setVisibility(ModifierKind.FINAL);
    }
}
