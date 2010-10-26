/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
import com.yapnu.adt.model.IntegerAdt;
import java.util.ArrayList;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 *
 * @author adrien
 */
public class AxiomTest {

    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomBothSideAreEquals() {
        Adt adt = BooleanAdt.instance().getAdt();
        Term term = adt.getOperationSignature("not").instantiates(adt.getConstant("false"));
        new Axiom(term, term);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithNullLeftTerm() {
        Adt adt = BooleanAdt.instance().getAdt();        
        new Axiom(null, adt.getOperationSignature("not").instantiates(adt.getConstant("false")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithNullRightTerm() {
        Adt adt = BooleanAdt.instance().getAdt();
        new Axiom(adt.getOperationSignature("not").instantiates(adt.getConstant("false")), null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithPreconditionNotInBoolSort() {
        Adt boolAdt = BooleanAdt.instance().getAdt();        
        Adt intAdt = IntegerAdt.instance().getAdt();
        
        new Axiom(intAdt.getConstant("0"), boolAdt.getConstant("false"), boolAdt.getConstant("true"));
    }

   @Test(expected=IllegalArgumentException.class)
   public void testCreateAxiomWithVariableWithSameNameButDifferentTypesBetweenTerms() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term leftTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));
        Term rightTerm = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        new Axiom(leftTerm, rightTerm);
    }

   @Test(expected=IllegalArgumentException.class)
   public void testCreateAxiomWithVariableWithSameNameButDifferentTypesBetewenLeftTermAndPrecondition() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term precondition = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        Term leftTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));        
        new Axiom(precondition, leftTerm, boolAdt.getConstant("false"));
    }

   @Test(expected=IllegalArgumentException.class)
   public void testCreateAxiomWithVariableWithSameNameButDifferentTypesBetewenRightTermAndPrecondition() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term precondition = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        Term rightTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));
        new Axiom(precondition, boolAdt.getConstant("false"), rightTerm);
    }

   @Test(expected=IllegalArgumentException.class)
   public void testCreateAxiomWithOccursCheck() {
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term leftTerm = intAdt.getVariable("x");
        Term rightTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));
        
        new Axiom(leftTerm, rightTerm);
    }

   @Test(expected=IllegalArgumentException.class)
   public void testThereIsLessOrEqualsVariablesInPreconditionCompareToLeftTerm() {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        Term precondition = boolAdt.getOperationSignature("and").instantiates(boolAdt.getVariable("x"), boolAdt.getVariable("y"));
        Term leftTerm = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        Term rightTerm = boolAdt.getVariable("x");

        new Axiom(precondition, leftTerm, rightTerm);
    }

   @Test(expected=IllegalArgumentException.class)
   public void testCreateAxiomPreconditionMustContainSomeVariables() {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        Term precondition = boolAdt.getOperationSignature("not").instantiates(boolAdt.getConstant("true"));
        Term leftTerm = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        Term rightTerm = boolAdt.getVariable("x");

        new Axiom(precondition, leftTerm, rightTerm);
    }

   @Test
   public void testIsValueObject() {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        Axiom axiom = new Axiom(boolAdt.getVariable("x"), boolAdt.getConstant("false"));
        Axiom axiomCloned = new Axiom(boolAdt.getVariable("x"), boolAdt.getConstant("false"));
        Axiom axiomWithPrecondition = new Axiom(boolAdt.getVariable("x"), boolAdt.getVariable("x"), boolAdt.getConstant("false"));
        Axiom axiomWithDifferentPrecondition = new Axiom(boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x")), boolAdt.getVariable("x"), boolAdt.getConstant("false"));
        Axiom axiomWithDifferentRightTerm = new Axiom(boolAdt.getVariable("x"), boolAdt.getOperationSignature("not").instantiates(boolAdt.getConstant("false")));
        Axiom axiomWithDifferentLeftTerm = new Axiom(boolAdt.getOperationSignature("not").instantiates(boolAdt.getConstant("false")), boolAdt.getConstant("true"));

        assertTrue("same objects", axiom.equals(axiomCloned));
        assertTrue("symmetry", axiomCloned.equals(axiom));
        assertFalse("only one has a precondition", axiom.equals(axiomWithPrecondition));
        assertFalse("precondition are differents", axiomWithPrecondition.equals(axiomWithDifferentPrecondition));
        assertFalse("left  terms are differents", axiom.equals(axiomWithDifferentLeftTerm));
        assertFalse("right terms are differents", axiom.equals(axiomWithDifferentRightTerm));

        assertTrue("same objects => same hashcode", axiom.hashCode() == axiomCloned.hashCode());
        assertFalse("left terms are differents => differents hashcode", axiom.hashCode() == axiomWithDifferentLeftTerm.hashCode());
        assertTrue("same left terms (even if right terms are differents) => same hashcode", axiom.hashCode() == axiomWithDifferentRightTerm.hashCode());
        assertTrue("same left terms (even if preconditions are differents) => same hashcode", axiomWithPrecondition.hashCode() == axiomWithDifferentPrecondition.hashCode());
        assertTrue("same left terms (even if only one has a precondition) => same hashcode", axiom.hashCode() == axiomWithPrecondition.hashCode());
   }

   @Test
   public void TestTryGetMatchingSubstitutionsWithoutPrecondition() {
       // Note: we don't test all the cases because it's already done in the test classes of the terms.

       Adt adt = IntegerAdt.instance().getAdt();
       
       OperationSignature addSignature = adt.getOperationSignature("add");
       Constant zero = adt.getConstant("0");
       Variable x = adt.getVariable("x");
       OperationSignature succSignature = adt.getOperationSignature("succ");
              
       Axiom axiom = new Axiom(addSignature.instantiates(zero, x), x);

       SubstitutionBag subsitutions = new SubstitutionBag();
       Term term = addSignature.instantiates(zero, succSignature.instantiates(zero));
       assertTrue(axiom.tryGetMatchingSubstitutions(null, term, subsitutions));
       assertTrue(subsitutions.getValue(x).equals(succSignature.instantiates(zero)));
   }

   @Test
   public void TestTryGetMatchingSubstitutionsWithPrecondition() {
       Adt intAdt = IntegerAdt.instance().getAdt();
       Adt boolAdt = BooleanAdt.instance().getAdt();
       ArrayList<Adt> adts = new ArrayList<Adt>();
       adts.add(intAdt);
       adts.add(boolAdt);
       TermRewritter termRewritter = new TermRewritter(adts);

       OperationSignature addSignature = intAdt.getOperationSignature("add");
       Constant zero = intAdt.getConstant("0");
       Variable x = intAdt.getVariable("x");
       OperationSignature succSignature = intAdt.getOperationSignature("succ");

       Term precondition = intAdt.getOperationSignature(">").instantiates(x, zero);
       Axiom axiom = new Axiom(precondition, addSignature.instantiates(zero, x), x);

       SubstitutionBag subsitutions = new SubstitutionBag();
       Term term = addSignature.instantiates(zero, succSignature.instantiates(zero));
       assertTrue(axiom.tryGetMatchingSubstitutions(termRewritter, term, subsitutions));
       assertTrue(subsitutions.getValue(x).equals(succSignature.instantiates(zero)));

       subsitutions.clear();
       assertFalse(axiom.tryGetMatchingSubstitutions(termRewritter, addSignature.instantiates(zero, zero), subsitutions));
   }
}