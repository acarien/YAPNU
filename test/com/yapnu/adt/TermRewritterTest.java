/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
import com.yapnu.adt.model.IntegerAdt;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class TermRewritterTest {


    /**
     * Test of Rewritte method, of class TermRewritter.
     */    
    @Test
    public void testRewritteGenerators() {
        Adt adt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);
        
        Term rewrittenTerm1 = rewritter.rewritte(adt.getConstant("true"));
        assertEquals(rewrittenTerm1, adt.getConstant("true"));

        Term rewrittenTerm2 = rewritter.rewritte(adt.getConstant("false"));
        assertEquals(rewrittenTerm2, adt.getConstant("false"));
    }

    /**
     * Test of Rewritte method, of class TermRewritter.
     */    
    @Test
    public void testRewritteAlreadyDefinedAxiom() {
        Adt boolAdt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter1 = new TermRewritter(boolAdt);

        Term finalTerm = boolAdt.getOperationSignature("not").instantiates(boolAdt.getConstant("false"));
        assertEquals("rewritte an already defined axiom", rewritter1.rewritte(finalTerm), boolAdt.getConstant("true"));


        Adt intAdt = IntegerAdt.instance().getAdt();
        TermRewritter rewritter2 = new TermRewritter(intAdt);
        intAdt.addTerm(new Constant("2", intAdt.getSort(), false));
        OperationSignature succ = intAdt.getOperationSignature("succ");
        Term two = succ.instantiates(succ.instantiates(intAdt.getConstant("0")));
        intAdt.addAxiom(new Axiom(intAdt.getConstant("2"), two));
        assertEquals("rewritte an already defined axiom", rewritter2.rewritte(intAdt.getConstant("2")), two);
    }
    
    @Test
    public void testRewritteComplexTerm() {
        Adt adt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);

        Constant falseTerm = adt.getConstant("false");
        Constant trueTerm = adt.getConstant("true");

        OperationSignature and = adt.getOperationSignature("and");
        OperationSignature or = adt.getOperationSignature("or");
        OperationSignature not = adt.getOperationSignature("not");
                
        Term term = or.instantiates(falseTerm, and.instantiates(trueTerm, not.instantiates(falseTerm)));

        Term rewrittenTerm1 = rewritter.rewritte(term);
        assertEquals(rewrittenTerm1, adt.getConstant("true"));
    }
    
    @Test
    public void testRewritteShortcutTerm() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);
        
        Constant falseTerm = new Constant("false", adt.getSort());
        Constant trueTerm = new Constant("true", adt.getSort());
        adt.addTerm(falseTerm);
        adt.addTerm(trueTerm);
        
        OperationSignature andSignature = new OperationSignature("and", false, adt.getSort(), adt.getSort(), adt.getSort());
        Variable variableX = new Variable("x", adt.getSort());
                                    
        Term axiomLeftTerm = andSignature.instantiates(falseTerm, variableX);
        Axiom axiom = new Axiom(axiomLeftTerm, falseTerm);
        adt.addAxiom(axiom);
        
        Term testTerm = andSignature.instantiates(falseTerm, trueTerm);

        Term rewrittenTerm1 = rewritter.rewritte(testTerm);
        assertEquals(rewrittenTerm1, falseTerm);
    }
    
    @Test
    public void testRewritteVariableNotContainedInAxiom() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);
               
        Variable variableX = new Variable("x", adt.getSort());                      
                
        Term rewrittenTerm1 = rewritter.rewritte(variableX);
        assertEquals(rewrittenTerm1, variableX);        
    }
    
    @Test
    public void testRewritteVariableContainedInAxiom() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);

        Constant falseTerm = new Constant("false", adt.getSort());
        adt.addTerm(falseTerm);

        Variable variableX = new Variable("x", adt.getSort());

        Axiom axiom = new Axiom(variableX, falseTerm);
        adt.addAxiom(axiom);

        Term rewrittenTerm1 = rewritter.rewritte(variableX);
        assertEquals(rewrittenTerm1, falseTerm);
    }
    
    @Test
    public void testRewritteAddFunction() {
        Adt adt = IntegerAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);

        Constant zero = adt.getConstant("0");
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");
                               
        Term rewrittenTerm1 = rewritter.rewritte(addSignature.instantiates(zero, succSignature.instantiates(zero)));
        assertEquals(rewrittenTerm1, succSignature.instantiates(zero));
        
        Term rewrittenTerm2 = rewritter.rewritte(
                addSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero)),
                succSignature.instantiates(zero)));
        assertEquals(rewrittenTerm2, succSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero))));
    }

    @Test
    public void testRewritteEqualsFunction() {
        ArrayList<Adt> adts = new ArrayList<Adt>();
        Adt intAdt = IntegerAdt.instance().getAdt();
        Adt boolAdt = BooleanAdt.instance().getAdt();
        adts.add(intAdt);
        adts.add(boolAdt);

        TermRewritter rewritter = new TermRewritter(adts);

        Constant zero = intAdt.getConstant("0");
        Constant falseTerm = boolAdt.getConstant("false");
        Constant trueTerm = boolAdt.getConstant("true");        
        OperationSignature succSignature = intAdt.getOperationSignature("succ");
        OperationSignature equalsSignature = intAdt.getOperationSignature("=");
        
        Term rewrittenTerm1 = rewritter.rewritte(equalsSignature.instantiates(zero, zero));
        assertEquals(rewrittenTerm1, trueTerm);
        
        Term rewrittenTerm2 = rewritter.rewritte(equalsSignature.instantiates(zero, succSignature.instantiates(zero)));
        assertEquals(rewrittenTerm2, falseTerm);
        
        Term rewrittenTerm3 = rewritter.rewritte(equalsSignature.instantiates(succSignature.instantiates(zero), zero));
        assertEquals(rewrittenTerm3, falseTerm);
        
        Term rewrittenTerm4 = rewritter.rewritte(equalsSignature.instantiates(succSignature.instantiates(zero), succSignature.instantiates(zero)));
        assertEquals(rewrittenTerm4, trueTerm);
        
        Term rewrittenTerm = rewritter.rewritte(equalsSignature.instantiates(succSignature.instantiates(succSignature.instantiates(zero)), succSignature.instantiates(zero)));
        assertEquals(rewrittenTerm, falseTerm);        
    }

    @Test
    public void testRewritteStrangeDomainFunction() {
        ArrayList<Adt> adts = new ArrayList<Adt>();
        Adt intAdt = IntegerAdt.instance().getAdt();
        Adt boolAdt = BooleanAdt.instance().getAdt();
        adts.add(intAdt);
        adts.add(boolAdt);

        TermRewritter rewritter = new TermRewritter(adts);

        Constant zero = intAdt.getConstant("0");
        Constant trueTerm = boolAdt.getConstant("true");
        Constant falseTerm = boolAdt.getConstant("false");

        OperationSignature equalsSignature = new OperationSignature("dummy", false, intAdt.getSort(), intAdt.getSort(), boolAdt.getSort());
        intAdt.addAxiom(new Axiom(equalsSignature.instantiates(zero, falseTerm), zero));

        Operation and = boolAdt.getOperationSignature("and").instantiates(trueTerm, falseTerm);
        Term rewrittenTerm = rewritter.rewritte(equalsSignature.instantiates(zero, and));
        assertEquals(rewrittenTerm, zero);
    }

    @Test
    public void testRewritteAawd() {
        Adt intAdt = IntegerAdt.instance().getAdt();
        OperationSignature fSignature = new OperationSignature("f", false, intAdt.getSort(), intAdt.getSort(), intAdt.getSort());
        OperationSignature gSignature = new OperationSignature("g", false, intAdt.getSort(), intAdt.getSort());        
        intAdt.addOperationSignature(fSignature);
        intAdt.addOperationSignature(gSignature);
        Variable varX = intAdt.getVariable("x");
        Variable varY = intAdt.getVariable("y");
        Axiom axiom = new Axiom(gSignature.instantiates(varY), varY);
        intAdt.addAxiom(axiom);

        ArrayList<Adt> adts = new ArrayList<Adt>();
        adts.add(intAdt);

        TermRewritter rewritter = new TermRewritter(adts);
        Term originalTerm = fSignature.instantiates(gSignature.instantiates(varX), varX);
                        
        Term rewrittenTerm = rewritter.rewritte(originalTerm);
        assertEquals(rewrittenTerm, fSignature.instantiates(varX, varX));
    }
}