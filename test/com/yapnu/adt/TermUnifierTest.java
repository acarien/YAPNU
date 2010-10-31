/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
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
        assertTrue(tmp[0].getValue(y).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[0].getValue(x).equals(zero));
        assertTrue(tmp[0].getValue(z).equals(zero));

        assertTrue(tmp[1].getValue(z).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[1].getValue(x).equals(zero));
        assertTrue(tmp[1].getValue(y).equals(zero));

        assertTrue(tmp[2].getValue(x).equals(succSignature.instantiates(zero)));
        assertTrue(tmp[2].getValue(y).equals(zero));
        assertTrue(tmp[2].getValue(z).equals(zero));
    }

    @Test
    public void testCanUnifies15() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertFalse(unifer.canUnify(succSignature.instantiates(zero), zero, bags));
        assertTrue(bags.size() == 0);
    }

    @Test
    public void testCanUnifies16() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(addSignature.instantiates(zero, zero), zero, bags));
        assertTrue(bags.size() == 0);
    }

    @Test
    public void testCanUnifies17() {
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifer.canUnify(succSignature.instantiates(x), succSignature.instantiates(zero), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].size() == 1);
        assertTrue(tmp[0].getValue(x).equals(zero));
    }

    @Test
    public void testCanUnifies18() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Sort sort = new Sort("sort");
        Adt adt = new Adt(sort);

        OperationSignature nSignature = new OperationSignature("n", false, sort, sort);
        adt.addOperationSignature(nSignature);

        OperationSignature equalsSignature = new OperationSignature("=", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(equalsSignature);

        Variable varX = new Variable("x", sort);
        Constant aCte = new Constant("a", sort);
        Constant bCte = new Constant("b", sort);
        adt.addTerm(aCte);
        adt.addTerm(bCte);

        adt.addAxiom(new Axiom(equalsSignature.instantiates(aCte, aCte), boolAdt.getConstant("true")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(aCte, bCte), boolAdt.getConstant("false")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(bCte, aCte), boolAdt.getConstant("false")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(bCte, bCte), boolAdt.getConstant("true")));

        adt.addAxiom(new Axiom(equalsSignature.instantiates(varX, bCte), nSignature.instantiates(varX), varX));

        TermUnifier unifier = new TermUnifier(adt, new TermRewritter(adt));
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifier.canUnify(nSignature.instantiates(varX), bCte, bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(varX).equals(bCte));

        bags.clear();
        assertFalse(unifier.canUnify(nSignature.instantiates(varX), aCte, bags));
    }

    @Test
    public void testCanUnifyWithPrecondition() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        Sort sort = new Sort("sort");
        Adt adt = new Adt(sort);

        OperationSignature nSignature = new OperationSignature("n", false, sort, sort);
        adt.addOperationSignature(nSignature);

        OperationSignature equalsSignature = new OperationSignature("=", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(equalsSignature);

        Variable varX = new Variable("x", sort);
        Constant aCte = new Constant("a", sort);
        Constant bCte = new Constant("b", sort);
        adt.addTerm(aCte);
        adt.addTerm(bCte);

        adt.addAxiom(new Axiom(equalsSignature.instantiates(aCte, aCte), boolAdt.getConstant("true")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(aCte, bCte), boolAdt.getConstant("false")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(bCte, aCte), boolAdt.getConstant("false")));
        adt.addAxiom(new Axiom(equalsSignature.instantiates(bCte, bCte), boolAdt.getConstant("true")));

        adt.addAxiom(new Axiom(equalsSignature.instantiates(varX, bCte), nSignature.instantiates(varX), aCte));

        TermUnifier unifier = new TermUnifier(adt, new TermRewritter(adt));
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifier.canUnify(nSignature.instantiates(varX), aCte, bags));

        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(varX).equals(bCte));
    }

    @Test
    public void testCanUnifyWithFreeVariable() {
        IntAdt2 intAdt2 = new IntAdt2();
        Adt adt = intAdt2.getAdt();

        Variable varX = adt.getVariable("x");
        Constant zeroCte = adt.getConstant("0");
        OperationSignature proj2 = adt.getOperationSignature("proj2");
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");

        TermUnifier unifier = new TermUnifier(adt, new TermRewritter(adt));
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifier.canUnify(proj2.instantiates(varX, zeroCte), zeroCte, bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(varX) instanceof Variable);
        assertTrue(tmp[0].isFreeVariable(varX));
        
        bags.clear();
        assertTrue(unifier.canUnify(addSignature.instantiates(varX, proj2.instantiates(varX, zeroCte)), succSignature.instantiates(zeroCte), bags));


        
        //assertTrue(unifier.canUnify(addSignature.instantiates(varX, projSignature.instantiates(varX, varY)), succSignature.instantiates(zeroCte), bags));
    }

    @Test
    public void testCanUnifyWithFreeVariableConstrainedWithOtherTerm() {
        IntAdt2 intAdt2 = new IntAdt2();
        Adt adt = intAdt2.getAdt();

        Variable varX = adt.getVariable("x");
        Constant zeroCte = adt.getConstant("0");
        OperationSignature proj2 = adt.getOperationSignature("proj2");
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");

        TermUnifier unifier = new TermUnifier(adt, new TermRewritter(adt));
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifier.canUnify(addSignature.instantiates(varX, proj2.instantiates(varX, zeroCte)), succSignature.instantiates(zeroCte), bags));
        assertTrue(bags.size() == 1);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);
        assertTrue(tmp[0].getValue(varX).equals(succSignature.instantiates(zeroCte)));
                
        //assertTrue(unifier.canUnify(addSignature.instantiates(varX, projSignature.instantiates(varX, varY)), succSignature.instantiates(zeroCte), bags));
    }

    @Test
    public void testCanUnifyWithFreeVariableConstrainedWithOtherTerm2() {
        IntAdt2 intAdt2 = new IntAdt2();
        Adt adt = intAdt2.getAdt();

        Variable varX = adt.getVariable("x");
        Variable varY = adt.getVariable("y");
        Constant zeroCte = adt.getConstant("0");
        OperationSignature proj2 = adt.getOperationSignature("proj2");
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");

        TermUnifier unifier = new TermUnifier(adt, new TermRewritter(adt));
        Set<SubstitutionBag> bags = new HashSet<SubstitutionBag>();
        assertTrue(unifier.canUnify(addSignature.instantiates(varX, proj2.instantiates(varX, varY)), succSignature.instantiates(zeroCte), bags));
        assertTrue(bags.size() == 2);
        SubstitutionBag[] tmp = new SubstitutionBag[bags.size()];
        bags.toArray(tmp);        
        assertTrue(tmp[0].getValue(varX).equals(succSignature.instantiates(zeroCte)));
        assertTrue(tmp[0].getValue(varY).equals(zeroCte));

        assertTrue(tmp[1].getValue(varX).equals(zeroCte));
        assertTrue(tmp[1].getValue(varY).equals(succSignature.instantiates(zeroCte)));
    }


    private class IntAdt2 {
        private final Sort sort = new Sort("sort");
        private Adt adt;

        public Adt getAdt() {
            if (adt == null) {
                this.build();
            }
            
            return adt;
        }

        private void build() {
            adt = new Adt(sort);

            OperationSignature proj2Signature = new OperationSignature("proj2", false, sort, sort, sort);
            adt.addOperationSignature(proj2Signature);

            OperationSignature succSignature = new OperationSignature("succ", true, sort, sort);
            adt.addOperationSignature(succSignature);

            OperationSignature addSignature = new OperationSignature("add", false, sort, sort, sort);
            adt.addOperationSignature(addSignature);

            Variable varX = new Variable("x", sort);
            Variable varY = new Variable("y", sort);
            adt.addTerm(varX);
            adt.addTerm(varY);
            
            Constant zeroCte = new Constant("0", sort);
            adt.addTerm(zeroCte);

            adt.addAxiom(new Axiom(proj2Signature.instantiates(varX, varY), varY));
            adt.addAxiom(new Axiom(addSignature.instantiates(zeroCte, varX), varX));
            adt.addAxiom(new Axiom(addSignature.instantiates(varX, zeroCte), varX));
            adt.addAxiom(new Axiom(addSignature.instantiates(succSignature.instantiates(varX), varY), succSignature.instantiates(addSignature.instantiates(varX, varY))));
        }
    }
}
