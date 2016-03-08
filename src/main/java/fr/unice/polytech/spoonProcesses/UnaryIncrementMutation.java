package fr.unice.polytech.spoonProcesses;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.*;

import java.security.spec.ECField;

/**
 * Created by SARROCHE Nicolas on 07/03/16.
 */
public class UnaryIncrementMutation extends AbstractProcessor<CtElement> {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtUnaryOperator;
    }


    @Override
    public void process(CtElement candidate) {
        if (!(candidate instanceof CtUnaryOperator)) {
            return;
        }

        CtUnaryOperator op = (CtUnaryOperator) candidate;
        CtElement parent = null;
        try {
            parent = candidate.getParent();
        }catch (Exception e){
            // passe toujours par là
            System.out.println("No parent for " + candidate);
        }
        // toujours false
        if(parent != null && parent instanceof CtFor){
            CtOperatorAssignmentImpl a = new CtOperatorAssignmentImpl();
            a.setKind(BinaryOperatorKind.PLUS);
            a.setAssigned(op.getOperand());
            a.setAssignment(new CtLiteralImpl<Integer>().setValue(3));
            ((CtFor) parent).getForUpdate().remove(op);
            ((CtFor) parent).addForUpdate(a);
        }else{
            // passe toujours là
            // Ne marche pas chez moi
            CtOperatorAssignmentImpl a = new CtOperatorAssignmentImpl   ();
            a.setKind(BinaryOperatorKind.PLUS);
            a.setAssigned(op.getOperand());
            a.setAssignment(new CtLiteralImpl<Integer>().setValue(3));
            //System.out.println("assign : " + a);
            System.out.println("Before : " + candidate);
            candidate.replace(a);
            System.out.println("After : "+ candidate);

            // ne marche pas non plus chez moi
            /*System.out.println(new CtCodeSnippetStatementImpl().setValue("" + op.getOperand() + " += 3").getValue());
            candidate.replace(new CtCodeSnippetStatementImpl().setValue("" + op.getOperand() + " += 3"));
            System.out.println(candidate);*/
        }
    }
}
