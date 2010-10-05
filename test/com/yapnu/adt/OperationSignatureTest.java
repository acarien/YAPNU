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
        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(sort);
        domain.add(sort);

        OperationSignature term1 = new OperationSignature("term", domain, sort, false);
        OperationSignature term1Cloned = new OperationSignature("term", domain, sort, false);
        OperationSignature term2 = new OperationSignature("term2", domain, sort, false);
        OperationSignature term3 = new OperationSignature("term", anotherSort, sort, false);

        ArrayList<Sort> anotherDomain = new ArrayList<Sort>();
        anotherDomain.add(sort);
        anotherDomain.add(anotherSort);

        OperationSignature term4 = new OperationSignature("term", anotherDomain, sort, false);
        OperationSignature term5 = new OperationSignature("term", domain, anotherSort, false);

        assertEquals("equals", term1, term1Cloned);
        assertEquals("symmetry", term1Cloned, term1);
        assertNotSame("different name", term1, term2);
        assertNotSame("different domain (1)", term1, term3);
        assertNotSame("different domain (2)", term1, term4);
        assertNotSame("different codomain", term1, term5);
    }

    public void testIsGenerator() {
        ArrayList<Sort> domain = new ArrayList<Sort>();

        assertFalse(new OperationSignature("term", domain, sort, false).isGenerator());
        assertTrue(new OperationSignature("term", domain, sort, true).isGenerator());
    }

    /*public void testIsGenerator() {
        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(sort);

        //Operation operation = new OperationSignature("term", domain, sort, false).instantiates(parameters));
    }*/

}