/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class SubstitutionBagTest {

    Sort sort = new Sort("bool");
    Constant constant1 = new Constant("cte1", sort);
    Constant constant2 = new Constant("cte2", sort);

    /**
     * Test of tryAddSubstitution method, of class SubstitutionBag.
     */
    @Test
    public void testTryAddSubstitution() {        
        Variable variableX1 = new Variable("x", sort);
        Variable variableX2 = new Variable("x", sort);
        Variable variableY = new Variable("y", sort);        
                        
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
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);
        Variable variableX3 = new Variable("x3", sort);                                     

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
    public void testCanSubstituteAVariableAsSubstitutionByAnotherTerm() {        
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);        

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertTrue(substitutions.tryAddSubstitution(variableX0, constant1));
        assertTrue(substitutions.getValue(variableX0).equals(constant1));
    }

    @Test
    public void testCanSubstituteAVariableAsSubstitutionByAnotherTerm2() {
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX2));
        assertTrue(substitutions.tryAddSubstitution(variableX0, constant1));
        assertFalse(substitutions.tryAddSubstitution(variableX1, constant2));
        assertFalse(substitutions.tryAddSubstitution(variableX2, constant2));
        assertTrue(substitutions.getValue(variableX0).equals(constant1));
    }

    @Test
    public void testCanSubstituteASubstitutionByAVariable() {        
        Variable variableX0 = new Variable("x0", sort);
        Variable variableX1 = new Variable("x1", sort);        

        SubstitutionBag substitutions = new SubstitutionBag();

        assertTrue(substitutions.tryAddSubstitution(variableX0, constant1));
        assertTrue(substitutions.tryAddSubstitution(variableX0, variableX1));
        assertFalse(substitutions.tryAddSubstitution(variableX1, constant2));
        assertTrue(substitutions.getValue(variableX0).equals(constant1));
        assertTrue(substitutions.getValue(variableX1).equals(constant1));
    }

    @Test
    public void testSizeAndClear() {        
        Variable variableX1 = new Variable("x", sort);        

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
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);
        
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

    @Test
    public void testIsFreeVariable() {        
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);

        assertFalse(new SubstitutionBag(new Substitution(variableX1, constant1)).isFreeVariable(variableX1));
        assertFalse(new SubstitutionBag(new Substitution(variableX1, variableX2), new Substitution(variableX2, constant2)).isFreeVariable(variableX1));
        assertTrue(new SubstitutionBag(new Substitution(variableX1, variableX2)).isFreeVariable(variableX1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRetainsAllNullSet() {
        SubstitutionBag bag = new SubstitutionBag();
        bag.retainsAll(null);
    }

    @Test
    public void testRetainsAll() {
        Variable variableX1 = new Variable("x1", sort);
        Variable variableX2 = new Variable("x2", sort);

        SubstitutionBag bag = new SubstitutionBag(new Substitution(variableX1, constant1));
        HashSet<Variable> toBeRemoved = new HashSet<Variable>();
        toBeRemoved.add(variableX1);

        bag.retainsAll(toBeRemoved);
        assertTrue("a variable is not removed if contained in the parameter", bag.hasSubstitution(variableX1));

        toBeRemoved.add(variableX2);
        bag.retainsAll(toBeRemoved);
        assertTrue("variables contained in the parameter that don't exist in the bag imply no modification", bag.hasSubstitution(variableX1));

        bag.tryAddSubstitution(variableX2, constant1);
        toBeRemoved.remove(variableX2);
        bag.retainsAll(toBeRemoved);
        assertTrue(bag.hasSubstitution(variableX1));
        assertFalse("the variable has been removed.", bag.hasSubstitution(variableX2));
    }
}