/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
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
       
        unifer = new TermUnifier(new AdtBag(intAdt));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of canUnifies method, of class TermUnifier.
     */
    @Test
    public void testCanUnifies1() {
        Unification unification = unifer.canUnify(zero, zero);
        assertTrue("input term (cte) exactly matches expected value", unification.isSuccess());
        assertTrue(unification.size() == 0);
    }

    @Test
    public void testCanUnifies2() {
        Unification unification = unifer.canUnify(x, succSignature.instantiates(zero));
        assertTrue("input term (variable) exactly matches expected value", unification.isSuccess());

        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, succSignature.instantiates(zero))));
        assertTrue(unification.equals(expectedUnification));        
    }

    @Test
    public void testCanUnifies3() {
        Unification unification = unifer.canUnify(x, succSignature.instantiates(succSignature.instantiates(zero)));
        assertTrue("input term (variable) exactly matches expected value", unification.isSuccess());
                
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, succSignature.instantiates(succSignature.instantiates(zero)))));
        assertTrue(unification.equals(expectedUnification));        
    }

    @Test
    public void testCanUnifies4() {
        Unification unification = unifer.canUnify(addSignature.instantiates(zero, succSignature.instantiates(zero)), succSignature.instantiates(zero));
        assertTrue("input term (operation) exactly matches expected value", unification.isSuccess());
        assertTrue(unification.size() == 0);
    }

    @Test
    public void testCanUnifies5() {
        Unification unification =unifer.canUnify(addSignature.instantiates(succSignature.instantiates(zero), zero), succSignature.instantiates(zero));
        assertTrue("input term (operation) exactly matches expected value", unification.isSuccess());
        assertTrue(unification.size() == 0);
    }

    @Test
    public void testCanUnifies6() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, zero), zero);
        assertTrue("input term matches expecting value through a substitution", unification.isSuccess());
        
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies7() {
        Unification unification = unifer.canUnify(addSignature.instantiates(zero, x), zero);
        assertTrue("input term matches expecting value through a substitution", unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies8() {
        Unification unification = unifer.canUnify(addSignature.instantiates(succSignature.instantiates(zero), y), succSignature.instantiates(zero));
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(y, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies9() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(zero));
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies10() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, succSignature.instantiates(zero)), succSignature.instantiates(succSignature.instantiates(zero)));
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, succSignature.instantiates(zero))));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies11() {
        Unification unification = unifer.canUnify(addSignature.instantiates(succSignature.instantiates(x), succSignature.instantiates(succSignature.instantiates(zero))), succSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero))));
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies12() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, y), zero);
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero), new Substitution(y, zero)));
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies13() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, y), succSignature.instantiates(zero));
        assertTrue(unification.isSuccess());       

        SubstitutionBag expectedSubs1 = new SubstitutionBag(new Substitution(x, zero), new Substitution(y, succSignature.instantiates(zero)));
        SubstitutionBag expectedSubs2 = new SubstitutionBag(new Substitution(y, zero), new Substitution(x, succSignature.instantiates(zero)));
        Unification expectedUnification = new Unification(expectedSubs1, expectedSubs2);
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies14() {
        Unification unification = unifer.canUnify(addSignature.instantiates(x, addSignature.instantiates(y, z)), succSignature.instantiates(zero));
        assertTrue(unification.isSuccess());

        SubstitutionBag expectedSubs1 = new SubstitutionBag(new Substitution(x, succSignature.instantiates(zero)), new Substitution(y, zero), new Substitution(z, zero));
        SubstitutionBag expectedSubs2 = new SubstitutionBag(new Substitution(y, succSignature.instantiates(zero)), new Substitution(x, zero), new Substitution(z, zero));
        SubstitutionBag expectedSubs3 = new SubstitutionBag(new Substitution(z, succSignature.instantiates(zero)), new Substitution(x, zero), new Substitution(y, zero));
        Unification expectedUnification = new Unification(expectedSubs1, expectedSubs2, expectedSubs3);
        assertTrue(unification.equals(expectedUnification));
    }

    @Test
    public void testCanUnifies15() {
        Unification unification = unifer.canUnify(succSignature.instantiates(zero), zero);
        assertFalse(unification.isSuccess());
        assertTrue(unification.size() == 0);
    }

    @Test
    public void testCanUnifies16() {
        Unification unification = unifer.canUnify(addSignature.instantiates(zero, zero), zero);
        assertTrue(unification.isSuccess());
        assertTrue(unification.size() == 0);
    }

    @Test
    public void testCanUnifies17() {
        Unification unification = unifer.canUnify(succSignature.instantiates(x), succSignature.instantiates(zero));
        assertTrue(unification.isSuccess());
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(x, zero)));
        assertTrue(unification.equals(expectedUnification));
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

        TermUnifier unifier = new TermUnifier(new AdtBag(adt));
        Unification unification = unifier.canUnify(nSignature.instantiates(varX), bCte);
        assertTrue(unification.isSuccess());

        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(varX, bCte)));
        assertTrue(unification.equals(expectedUnification));
        
        unification.clear();        
        assertFalse(unifier.canUnify(nSignature.instantiates(varX), aCte).isSuccess());
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

        TermUnifier unifier = new TermUnifier(new AdtBag(adt));
        Unification unification = unifier.canUnify(nSignature.instantiates(varX), aCte);
        assertTrue(unification.isSuccess());

        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(varX, bCte)));
        assertTrue(unification.equals(expectedUnification));
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

        TermUnifier unifier = new TermUnifier(new AdtBag(adt));
        Unification unification = unifier.canUnify(proj2.instantiates(varX, zeroCte), zeroCte);
        assertTrue(unification.isSuccess());
        assertTrue(unification.size() == 1);
        assertTrue(unification.getSubstitutions(varX).get(0).isFreeVariable());
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

        TermUnifier unifier = new TermUnifier(new AdtBag(adt));
        Unification unification = unifier.canUnify(addSignature.instantiates(varX, proj2.instantiates(varX, zeroCte)), succSignature.instantiates(zeroCte));
        assertTrue(unification.isSuccess());
        
        Unification expectedUnification = new Unification(new SubstitutionBag(new Substitution(varX, succSignature.instantiates(zeroCte))));
        assertTrue(unification.equals(expectedUnification));;
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

        TermUnifier unifier = new TermUnifier(new AdtBag(adt));
        Unification unification = unifier.canUnify(addSignature.instantiates(varX, proj2.instantiates(varX, varY)), succSignature.instantiates(zeroCte));
        assertTrue(unification.isSuccess());

        SubstitutionBag expectedSubs1 = new SubstitutionBag(new Substitution(varX, zeroCte), new Substitution(varY, succSignature.instantiates(zeroCte)));
        SubstitutionBag expectedSubs2 = new SubstitutionBag(new Substitution(varY, zeroCte), new Substitution(varX, succSignature.instantiates(zeroCte)));
        Unification expectedUnification = new Unification(expectedSubs1, expectedSubs2);
        assertTrue(unification.equals(expectedUnification));
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
