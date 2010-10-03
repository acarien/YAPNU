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
public class VariableTest {
    
    private final Sort sort = new Sort("sort");

    /**
     * Test of getSort method, of class Variable.
     */
    @Test
    public void testGetSort() {
        Variable variable = new Variable("x", sort);
        assertEquals(variable.getSort(), sort);
    }

    /**
     * Test of equals method, of class Variable.
     */
    @Test
    public void testEquals() {
        Variable v1 = new Variable("x", sort);
        Variable v1Cloned = new Variable("x", sort);
        Variable v2 = new Variable("y", sort);
        Variable v3 = new Variable("x", new Sort("anotherSort"));

        assertTrue("same objects", v1.equals(v1Cloned));
        assertTrue("symmetry", v1Cloned.equals(v1));
        assertFalse("difference on names", v1.equals(v2));
        assertFalse("difference on sorts", v1.equals(v3));
    }
}