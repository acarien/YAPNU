/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.yapnu.adt.model.BooleanAdt;
import com.yapnu.adt.model.IntegerAdt;
import java.util.ArrayList;
import org.junit.Ignore;
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
    @Ignore
    @Test
    public void testRewritteGenerators() {
        Adt adt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);
        
        Term rewrittenTerm1 = rewritter.Rewritte(adt.getConstant("true"));
        assertEquals(rewrittenTerm1, adt.getConstant("true"));

        Term rewrittenTerm2 = rewritter.Rewritte(adt.getConstant("false"));
        assertEquals(rewrittenTerm2, adt.getConstant("false"));
    }

    /**
     * Test of Rewritte method, of class TermRewritter.
     */
    @Ignore
    @Test
    public void testRewritteAlreadyDefinedAxiom() {
        Adt adt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);
        
        Term term = adt.getOperationSignature("not").instantiates(adt.getConstant("false"));

        Term rewrittenTerm1 = rewritter.Rewritte(term);
        assertEquals(rewrittenTerm1, adt.getConstant("true"));
    }

    @Ignore
    @Test
    public void testRewritteComplexTerm() {
        Adt adt = BooleanAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);

        Constant falseTerm = adt.getConstant("false");
        Constant trueTerm = adt.getConstant("true");

        OperationSignature and = adt.getOperationSignature("and");
        OperationSignature or = adt.getOperationSignature("or");
        OperationSignature not = adt.getOperationSignature("not");
        
        Term[] andParameters = {(Term)trueTerm, (Term)not.instantiates(falseTerm)};
        Term[] orParameters = {(Term)falseTerm, (Term)and.instantiates(andParameters)};

        Term term = or.instantiates(orParameters);

        Term rewrittenTerm1 = rewritter.Rewritte(term);
        assertEquals(rewrittenTerm1, adt.getConstant("true"));
    }

    @Ignore
    @Test
    public void testRewritteShortcutTerm() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);
        
        Constant falseTerm = new Constant("false", adt.getSort());
        Constant trueTerm = new Constant("true", adt.getSort());
        adt.addTerm(falseTerm);
        adt.addTerm(trueTerm);

        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(adt.getSort());
        domain.add(adt.getSort());

        OperationSignature andSignature = new OperationSignature("and", domain, adt.getSort(), false);
        Variable variableX = new Variable("x", adt.getSort());
                            
        Term[] axiomParameters = {falseTerm, variableX};
        Term axiomLeftTerm = andSignature.instantiates(axiomParameters);
        Axiom axiom = new Axiom(axiomLeftTerm, falseTerm);
        adt.addAxiom(axiom);

        Term[] testTermParameters = {falseTerm, trueTerm};
        Term testTerm = andSignature.instantiates(testTermParameters);

        Term rewrittenTerm1 = rewritter.Rewritte(testTerm);
        assertEquals(rewrittenTerm1, falseTerm);
    }

    @Ignore
    @Test(expected=IllegalArgumentException.class)
    public void testRewritteVariableNotContainedInAxiom() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);

        Constant falseTerm = new Constant("false", adt.getSort());        
        adt.addTerm(falseTerm);        
        
        Variable variableX = new Variable("x", adt.getSort());
               
        Axiom axiom = new Axiom(variableX, falseTerm);
                
        Term rewrittenTerm1 = rewritter.Rewritte(variableX);
        assertEquals(rewrittenTerm1, falseTerm);
    }

    @Ignore
    @Test
    public void testRewritteVariableContainedInAxiom() {
        Adt adt = new Adt(new Sort("bool"));
        TermRewritter rewritter = new TermRewritter(adt);

        Constant falseTerm = new Constant("false", adt.getSort());
        adt.addTerm(falseTerm);

        Variable variableX = new Variable("x", adt.getSort());

        Axiom axiom = new Axiom(variableX, falseTerm);
        adt.addAxiom(axiom);

        Term rewrittenTerm1 = rewritter.Rewritte(variableX);
        assertEquals(rewrittenTerm1, falseTerm);
    }

    @Ignore
    @Test
    public void testRewritteAddFunction() {
        Adt adt = IntegerAdt.instance().getAdt();
        TermRewritter rewritter = new TermRewritter(adt);

        Constant zero = adt.getConstant("0");
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");
                       
        Term[] baseCaseParamter = {zero, succSignature.instantiates(zero)};        
        Term rewrittenTerm1 = rewritter.Rewritte(addSignature.instantiates(baseCaseParamter));
        assertEquals(rewrittenTerm1, succSignature.instantiates(zero));

        Term[] nominalCaseParamter = {succSignature.instantiates(succSignature.instantiates(zero)), succSignature.instantiates(zero)};
        Term rewrittenTerm2 = rewritter.Rewritte(addSignature.instantiates(nominalCaseParamter));
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
        OperationSignature addSignature = intAdt.getOperationSignature("add");
        OperationSignature succSignature = intAdt.getOperationSignature("succ");
        OperationSignature equalsSignature = intAdt.getOperationSignature("=");

        Term[] baseCaseParamter = {zero, zero};
        Term rewrittenTerm1 = rewritter.Rewritte(equalsSignature.instantiates(baseCaseParamter));
        assertEquals(rewrittenTerm1, trueTerm);

        Term[] case2Paramter = {zero, succSignature.instantiates(zero)};
        Term rewrittenTerm2 = rewritter.Rewritte(equalsSignature.instantiates(case2Paramter));
        assertEquals(rewrittenTerm2, falseTerm);

        Term[] case3Paramter = {succSignature.instantiates(zero), zero};
        Term rewrittenTerm3 = rewritter.Rewritte(equalsSignature.instantiates(case3Paramter));
        assertEquals(rewrittenTerm3, falseTerm);

        Term[] case4Paramter = {succSignature.instantiates(zero), succSignature.instantiates(zero)};
        Term rewrittenTerm4 = rewritter.Rewritte(equalsSignature.instantiates(case4Paramter));
        assertEquals(rewrittenTerm4, trueTerm);

        Term[] case5Paramter = {succSignature.instantiates(succSignature.instantiates(zero)), succSignature.instantiates(zero)};
        Term rewrittenTerm = rewritter.Rewritte(equalsSignature.instantiates(case5Paramter));
        assertEquals(rewrittenTerm, falseTerm);        
    }
}