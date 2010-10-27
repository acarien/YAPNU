/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class SubstitutionBagTest {
   
    /**
     * Test of tryAddSubstitution method, of class SubstitutionBag.
     */
    @Test
    public void testTryAddSubstitution() {
        Sort sort = new Sort("bool");
        Variable variableX1 = new Variable("x", sort);
        Variable variableX2 = new Variable("x", sort);
        Variable variableY = new Variable("y", sort);
        Constant constant1 = new Constant("cte1", sort);
        Constant constant2 = new Constant("cte2", sort);
                        
        SubstitutionBag substitutions = new SubstitutionBag();
        
        boolean added1 = substitutions.tryAddSubstitution(variableX1, constant1);
        assertTrue(added1);
        assertTrue(substitutions.size() == 1);

        boolean added2 = substitutions.tryAddSubstitution(variableX2, constant1);
        assertTrue(added2);
        assertTrue(substitutions.size() == 1);

        boolean added3 = substitutions.tryAddSubstitution(variableX1, constant2);
        assertFalse(added3);
        assertTrue(substitutions.size() == 1);

        boolean added4 = substitutions.tryAddSubstitution(variableY, constant2);
        assertTrue(added4);
        assertTrue(substitutions.size() == 2);
    }

    @Test
    public void testTryGetSubstitutionWithDependencies() {
        Sort sort = new Sort("bool");
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);
        Variable variableX3 = new Variable("x3", sort);        
                
        Constant constant1 = new Constant("cte1", sort);        

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertTrue(substitutions.tryAddSubstitution(variableX1, variableX2));
        assertTrue(substitutions.tryAddSubstitution(variableX2, constant1));
        assertTrue(substitutions.getValue(variableX0).equals(constant1));
        assertTrue(substitutions.getValue(variableX1).equals(constant1));
        assertTrue(substitutions.getValue(variableX2).equals(constant1));

        assertTrue(substitutions.tryAddSubstitution(variableX3, variableX2));
        assertTrue(substitutions.getValue(variableX3).equals(constant1));
    }

    @Test
    public void awd() {
        Sort sort = new Sort("bool");
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);
        Constant constant = new Constant("cte1", sort);

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertTrue(substitutions.tryAddSubstitution(variableX0, constant));
        assertTrue(substitutions.getValue(variableX0).equals(constant));
    }

    @Test
    public void awd2() {
        Sort sort = new Sort("bool");
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);
        Constant constant = new Constant("cte1", sort);

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, constant));
        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertTrue(substitutions.getValue(variableX0).equals(constant));
        assertTrue(substitutions.getValue(variableX1).equals(constant));
    }

    @Test
    public void testSizeAndClear() {
        Sort sort = new Sort("bool");
        Variable variableX1 = new Variable("x", sort);
        Constant constant1 = new Constant("cte1", sort);

        SubstitutionBag substitutions = new SubstitutionBag();
        assertTrue(substitutions.size() == 0);

        boolean added1 = substitutions.tryAddSubstitution(variableX1, constant1);
        assertTrue(added1);

        assertTrue(substitutions.size() == 1);
        
        substitutions.clear();
        assertTrue(substitutions.size() == 0);
    }

    @Test
    public void testValueObject() {
        Sort sort = new Sort("bool");
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);
        Constant constant1 = new Constant("cte1", sort);
        Constant constant2 = new Constant("cte2", sort);

        SubstitutionBag bag1 = new SubstitutionBag();
        SubstitutionBag bag2 = new SubstitutionBag();
        assertTrue("empty bags are equals", bag1.equals(bag2));
        assertTrue("symmetry", bag2.equals(bag1));

        bag1.tryAddSubstitution(variableX1, constant1);
        bag2.tryAddSubstitution(variableX1, constant2);
        assertTrue("bags with different contents are not equal", !bag2.equals(bag1));

        bag1.clear();
        bag2.clear();
        bag1.tryAddSubstitution(variableX1, constant1);
        bag1.tryAddSubstitution(variableX2, constant1);

        bag2.tryAddSubstitution(variableX1, variableX2);
        bag2.tryAddSubstitution(variableX2, constant1);

        assertTrue("equality implies computing the dependencies", bag1.equals(bag2));
    }
}