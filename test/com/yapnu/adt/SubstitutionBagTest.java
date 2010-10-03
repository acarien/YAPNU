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

}