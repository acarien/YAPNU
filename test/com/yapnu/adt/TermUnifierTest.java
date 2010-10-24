/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.IntegerAdt;
import java.util.HashSet;
import java.util.Set;
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
    private Variable z;

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
        z = new Variable("z", intAdt.getSort());

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
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term (cte) exactly matches expected value", unifer.canUnify(zero, zero, bags));
        assertTrue(bags.size() == 0);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
    }

    @Test
    public void testCanUnifies2() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term (variable) exactly matches expected value", unifer.canUnify(x, succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(succSignature.instantiates(zero)));
    }

    @Test
    public void testCanUnifies3() {        
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term (variable) exactly matches expected value", unifer.canUnify(x, succSignature.instantiates(succSignature.instantiates(zero)), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(succSignature.instantiates(succSignature.instantiates(zero))));
    }

    @Test
    public void testCanUnifies4() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term (operation) exactly matches expected value", unifer.canUnify(addSignature.instantiates(zero, succSignature.instantiates(zero)), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 0);
    }

    @Test
    public void testCanUnifies5() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term (operation) exactly matches expected value", unifer.canUnify(addSignature.instantiates(succSignature.instantiates(zero), zero), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 0);
    }

    @Test
    public void testCanUnifies6() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term matches expecting value through a substitution", unifer.canUnify(addSignature.instantiates(x, zero), zero, bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies7() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue("input term matches expecting value through a substitution", unifer.canUnify(addSignature.instantiates(zero, x), zero, bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies8() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(succSignature.instantiates(zero), y), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(y).equals(zero));
    }

    @Test
    public void testCanUnifies9() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies10() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(succSignature.instantiates(zero)), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(succSignature.instantiates(zero)));
    }

    @Test
    public void testCanUnifies11() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(succSignature.instantiates(x), succSignature.instantiates(succSignature.instantiates(zero))), succSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero))), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies12() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(x, y), zero, bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
        assertTrue(tmp[0].getValue(y).equals(zero));
    }

    @Test
    public void testCanUnifies13() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(x, y), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 2);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
        assertTrue(tmp[0].getValue(y).equals(succSignature.instantiates(zero)));

        assertTrue(tmp[1].getValue(x).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[1].getValue(y).equals(zero));
    }

    @Test
    public void testCanUnifies14() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(x, addSignature.instantiates(y, z)), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 3);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(x).equals(zero));
        assertTrue(tmp[0].getValue(y).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[0].getValue(z).equals(zero));

        assertTrue(tmp[1].getValue(x).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[1].getValue(y).equals(zero));
        assertTrue(tmp[1].getValue(z).equals(zero));

        assertTrue(tmp[2].getValue(z).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[2].getValue(x).equals(zero));
        assertTrue(tmp[2].getValue(y).equals(zero));
    }
}