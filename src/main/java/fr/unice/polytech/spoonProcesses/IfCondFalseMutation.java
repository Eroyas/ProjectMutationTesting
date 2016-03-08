package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtLiteralImpl;

/**
 * Created by SARROCHE Nicolas on 07/03/16.
 */
public class IfCondFalseMutation extends AbstractProcessor<CtElement> {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtIf;
    }


    @Override
    public void process(CtElement candidate) {
        if (!(candidate instanceof CtIf)) {
            return;
        }

        CtIf op = (CtIf) candidate;
        op.getCondition().replace(new CtLiteralImpl<Boolean>().setValue(false));
    }
}
