/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.IntegerAdt;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class TermUnifierTest {

    private TermUnifier unifer;
    private Constant zero;
    private OperationSignature addSignature;
    private OperationSignature succSignature;
    private Variable x;
    private Variable y;

    @BeforeClass
    public static void setUpClass() throws Exception {
            }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Adt intAdt = IntegerAdt.instance().getAdt();

        zero = intAdt.getConstant("0");
        addSignature = intAdt.getOperationSignature("add");
        succSignature = intAdt.getOperationSignature("succ");
        x = new Variable("x", intAdt.getSort());
        y = new Variable("y", intAdt.getSort());

        unifer = new TermUnifier(intAdt, new TermRewritter(intAdt));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of canUnifies method, of class TermUnifier.
     */
    @Test
    public void testCanUnifies1() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term (cte) exactly matches expected value", unifer.canUnifies(zero, zero, bag));
        assertTrue(bag.size() == 0);
    }

    @Test
    public void testCanUnifies2() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term (variable) exactly matches expected value", unifer.canUnifies(x, succSignature.instantiates(zero), bag));        
        assertTrue(bag.getValue(x).equals(succSignature.instantiates(zero)));
    }

    @Test
    public void testCanUnifies3() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term (variable) exactly matches expected value", unifer.canUnifies(x, succSignature.instantiates(succSignature.instantiates(zero)), bag));
        assertTrue(bag.getValue(x).equals(succSignature.instantiates(succSignature.instantiates(zero))));
    }

    @Test
    public void testCanUnifies4() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term (operation) exactly matches expected value", unifer.canUnifies(addSignature.instantiates(zero, succSignature.instantiates(zero)), succSignature.instantiates(zero), bag));
        assertTrue(bag.size() == 0);
    }

    @Test
    public void testCanUnifies5() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term (operation) exactly matches expected value", unifer.canUnifies(addSignature.instantiates(succSignature.instantiates(zero), zero), succSignature.instantiates(zero), bag));
        assertTrue(bag.size() == 0);
    }

    @Test
    public void testCanUnifies6() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term matches expecting value through a substitution", unifer.canUnifies(addSignature.instantiates(x, zero), zero, bag));
        assertTrue(bag.getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies7() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("input term matches expecting value through a substitution", unifer.canUnifies(addSignature.instantiates(zero, x), zero, bag));
        assertTrue(bag.getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies8() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue(unifer.canUnifies(addSignature.instantiates(succSignature.instantiates(zero), y), succSignature.instantiates(zero), bag));
        assertTrue(bag.getValue(y).equals(zero));
    }

    @Test
    public void testCanUnifies9() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue(unifer.canUnifies(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(zero), bag));
        assertTrue(bag.getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies10() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue(unifer.canUnifies(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(succSignature.instantiates(zero)), bag));
        assertTrue(bag.getValue(x).equals(succSignature.instantiates(zero)));
    }

    @Test
    public void testCanUnifies11() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue(unifer.canUnifies(addSignature.instantiates(succSignature.instantiates(x), succSignature.instantiates(succSignature.instantiates(zero))), succSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero))), bag));
        assertTrue(bag.getValue(x).equals(zero));
    }    
}