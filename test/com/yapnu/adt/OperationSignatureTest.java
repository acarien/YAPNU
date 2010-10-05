/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.ArrayList;
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

    public void testIsGenerator() {
        assertFalse(new OperationSignature("term", false, sort, sort).isGenerator());
        assertTrue(new OperationSignature("term", true, sort, sort).isGenerator());
    }

    /*public void testIsGenerator() {
        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(sort);

        //Operation operation = new OperationSignature("term", domain, sort, false).instantiates(parameters));
    }*/

}