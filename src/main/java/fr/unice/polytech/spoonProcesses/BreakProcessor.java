package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBreak;

/**
 * @author tijani on 11/02/16.
 */
public class BreakProcessor extends AbstractProcessor<CtBreak> {

    @Override
    public void process(CtBreak elt) {
        elt.delete();
    }
}
