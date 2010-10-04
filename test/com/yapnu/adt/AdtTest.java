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
}