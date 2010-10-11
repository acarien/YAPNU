/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
import com.yapnu.adt.model.IntegerAdt;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class AxiomTest {

    @Test(expected=IllegalArgumentException.class)
    public void testBothSideAreEquals() {
        Adt adt = BooleanAdt.instance().getAdt();
        Term term = adt.getOperationSignature("not").instantiates(adt.getConstant("false"));
        Axiom axiom = new Axiom(term, term);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithNullLeftTerm() {
        Adt adt = BooleanAdt.instance().getAdt();        
        Axiom axiom = new Axiom(null, adt.getOperationSignature("not").instantiates(adt.getConstant("false")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithNullRightTerm() {
        Adt adt = BooleanAdt.instance().getAdt();
        Axiom axiom = new Axiom(adt.getOperationSignature("not").instantiates(adt.getConstant("false")), null);
    }

   @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithVariableWithSameNameButDifferentType() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term leftTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));
        Term rightTerm = boolAdt.getOperationSignature("not").instantiates(boolAdt.getVariable("x"));
        Axiom axiom = new Axiom(leftTerm, rightTerm);
    }

   @Test(expected=IllegalArgumentException.class)
    public void testCreateAxiomWithOccursCheck() {
        Adt intAdt = IntegerAdt.instance().getAdt();

        Term leftTerm = intAdt.getVariable("x");
        Term rightTerm = intAdt.getOperationSignature("add").instantiates(intAdt.getVariable("x"), intAdt.getConstant("0"));
        
        Axiom axiom = new Axiom(leftTerm, rightTerm);
    }

}