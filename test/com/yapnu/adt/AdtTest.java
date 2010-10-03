/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class AdtTest {

    private Adt adt;

    @Before
    public void setUp() {
        this.adt = new Adt(new Sort("int"));
    }

    /**
     * Test of getSort method, of class Adt.
     */
    @Test
    public void testGetSort() {
        Sort sort = new Sort("int");
        Adt instance = new Adt(sort);

        assertEquals(instance.getSort(), sort);
    }

    /**
     * Test of addTerm method, of class Adt.
     */
    @Test
    public void testAddConstant() {
        Constant c1a = new Constant("c1", adt.getSort());
        Constant c1b = new Constant("c1", adt.getSort());
        Constant c2 = new Constant("c2", new Sort("another sort"));
       
        adt.addTerm(c1a);
        adt.addTerm(c1b);
        adt.addTerm(c2);
    }




}