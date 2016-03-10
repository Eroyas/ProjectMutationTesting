package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtThisAccessImpl;

/**
 * Created by SARROCHE Nicolas on 08/03/16.
 */
public class ThisRemovalMutation extends AbstractProcessor<CtElement> {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtThisAccess;
    }


    @Override
    public void process(CtElement candidate) {
        // if the candidate doesn't match, return
        if (!(candidate instanceof CtThisAccess)) {
            return;
        }
        // remove "this"
        candidate.delete();
    }
}
