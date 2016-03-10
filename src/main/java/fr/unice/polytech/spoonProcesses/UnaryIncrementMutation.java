package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.*;

/**
 * Created by SARROCHE Nicolas on 07/03/16.
 */
public class UnaryIncrementMutation extends AbstractProcessor<CtElement> {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {

        return candidate instanceof CtFor;
    }


    @Override
    public void process(CtElement candidate) {
        if(!(candidate instanceof CtFor))
        {
            return;
        }

        CtForImpl ctFor = (CtForImpl) candidate;
        CtBinaryOperatorImpl expr = (CtBinaryOperatorImpl) ctFor.getExpression();
        if(expr.getKind().equals(BinaryOperatorKind.LT) || expr.getKind().equals(BinaryOperatorKind.GT))
        {
            expr.setKind(BinaryOperatorKind.LE);
        }
        System.out.println("############### EXPRESSION " + expr);




        }

    }
