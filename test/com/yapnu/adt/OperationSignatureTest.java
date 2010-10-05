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
public class OperationSignatureTest {

    private final Sort sort = new Sort("sort");

    /**
     * Test of equals method, of class OperationSignatureTerm.
     */
    @Test
    public void testEquals() {
        Sort anotherSort = new Sort("anotherSort");
        OperationSignature term1 = new OperationSignature("term", false, sort, sort, sort);
        OperationSignature term1Cloned = new OperationSignature("term", false, sort, sort, sort);
        OperationSignature term2 = new OperationSignature("term2", false, sort, sort, sort);
        OperationSignature term3 = new OperationSignature("term", false, anotherSort, sort);

        OperationSignature term4 = new OperationSignature("term", false, sort, sort, anotherSort);
        OperationSignature term5 = new OperationSignature("term", false, anotherSort, sort, sort);

        assertEquals("equals", term1, term1Cloned);
        assertEquals("symmetry", term1Cloned, term1);
        assertNotSame("different name", term1, term2);
        assertNotSame("different domain (1)", term1, term3);
        assertNotSame("different domain (2)", term1, term4);
        assertNotSame("different codomain", term1, term5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        new OperationSignature(null, true, sort, sort);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNoCodomain() {
        new OperationSignature("a name", true, null, sort);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNoDomain() {
        new OperationSignature("a name", true, sort, (Sort[]) null);
    }

    @Test
    public void testIsGenerator() {
        assertFalse(new OperationSignature("term", false, sort, sort).isGenerator());
        assertTrue(new OperationSignature("term", true, sort, sort).isGenerator());
    }

    @Test
    public void testInstantiation() {
        Constant cte1 = new Constant("cte1", sort);
        Constant cte2 = new Constant("cte2", sort);
        Operation operation = new OperationSignature("term", false, sort, sort, sort).instantiates(cte1, cte2);
        assertEquals(operation.getParamter(0), cte1);
        assertEquals(operation.getParamter(1), cte2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInstantiationWithWrongNumberOfArguments() {
        new OperationSignature("term", false, sort, sort, sort).instantiates(new Constant("cte1", sort));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInstantiationWithArgumentOfWrongSort() {
        new OperationSignature("term", false, sort).instantiates(new Constant("cte1", new Sort("anotherSort")));
    }
}